require_relative 'message'

class Chat

	attr_reader :user, :other, :messages

	def initialize(data, user)
		@user = user
		@other = scrape_other(data)
		@messages = scrape_messages(data) 
  end

	def missing_other?
    @other.to_s.empty?
  end

	private

	def scrape_other(data)
		other_data = data.css('.user') do |other|
			other.text != @user
		end	
		other_data.text
	end

	def scrape_messages(data)
		message_data = data.css('.message')
		message_data.map { |message| Message.new(message) }
	end
end