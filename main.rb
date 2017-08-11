require 'nokogiri'
require_relative 'scraper'

begin
  html = Nokogiri::HTML(open(ARGV[0]))  
  scraper = Scraper.new(html)
rescue Exception => e
  puts 'Invalid message file.'
  puts 'Usage: ruby main.rb <MESSAGE_FILE>'
end