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

self_name = page.css('title').text[0..-12]

# threads = page.css('div.thread')
# threads.each do |thread|
# 	other_name = thread.css('span.user').find { |other_name| other_name&.text != self_name }
# 	puts other_name
# end

