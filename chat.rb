require 'set'
require_relative 'message'

class Chat

	attr_reader :user, :others, :messages

	def initialize(data, user, others=nil, messages=nil)
		@user = user
		@others = others.nil? ? scrape_others(data) : others
		@messages = messages.nil? ? scrape_messages(data) : messages 
  end

  def missing?
  	@others == nil || @others.length == 0
  end

	def group?
		@others != nil && @others.length > 1
	end

	def +(chat)
		timeA = @messages[0].time
		timeB = chat.messages[0].time
		all = timeA < timeB ? @messages + chat.messages : chat.messages + @messages
		Chat.new(nil, @user, @others, all)			
	end

	private

	def scrape_others(data)
		Set.new(data.css('.user').map { |other| other.text.strip }).subtract [@user, '', nil]
	end

	def scrape_messages(data)
		data.css('.message').map { |message| Message.new(message) }.reverse
	end
end