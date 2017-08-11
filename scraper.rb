require 'nokogiri'
require 'time'
require_relative 'chat'

begin
    page = Nokogiri::HTML(open(ARGV[0]))
rescue Exception => e
    puts "Invalid message file."
    exit
end

puts 'Done parsing html'

user = page.css('h1')[0].text

chats = page.css('div.thread')

puts 'Done parsing chats'

chats = chats.map { |chat| Chat.new(chat, user) }

puts 'Done creating objects'

chats = chats.select { |chat| not chat.missing_other? }

puts 'Done filtering'

chats = chats.sort_by { |chat| chat.messages.length }
chats = chats.reverse

puts 'Done sorting'

chats.take(10).each do |chat|
  puts "#{chat.messages.length} messages with #{chat.other}"
end