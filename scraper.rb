require 'nokogiri'
require 'time'

def scrape_name(thread)
	thread.css
end

begin
    page = Nokogiri::HTML(open(ARGV[0]))
rescue Exception => e
    puts "Invalid message file."
    puts "Usage: ruby scraper.rb <MESSAGES_FILE>"
    exit
end

user = page.css('h1')[0].text
puts user

# threads = page.css('div.thread')
# threads.each do |thread|
# 	other_name = thread.css('span.user').find { |other_name| other_name&.text != self_name }
# 	puts other_name
# end

