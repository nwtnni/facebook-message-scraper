require 'set'
require_relative 'message'

class Chat

	attr_reader :user, :others, :messages

	def initialize(data, user)
		@user = user
		@others = scrape_others(data)
		@messages = scrape_messages(data) 
  end

  def missing?
  	@others.length == 0
  end

	def group?
		@others != nil && @others.length > 1
	end

	private

	def scrape_others(data)
		others = Set.new(data.css('.user').map { |other| other.text.strip })
		others.subtract [@user, '', nil]
	end

	def scrape_messages(data)
		message_data = data.css('.message')
		message_data.map { |message| Message.new(message) }
	end
end