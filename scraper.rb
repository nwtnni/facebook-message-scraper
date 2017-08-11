require 'nokogiri'
require 'time'
require_relative 'chat'

begin
    page = Nokogiri::HTML(open(ARGV[0]))
rescue Exception => e
    puts "Invalid message file."
    exit
end

user = page.css('h1')[0].text

chats = page.css('div.thread')
chats = chats.map { |chat| Chat.new(chat, user) }
chats = chats.select { |chat| !(chat.group? || chat.missing?) }
chats = chats.sort_by { |chat| chat.messages.length }
chats = chats.reverse

chats.each do |chat|
  puts "#{chat.messages.length} messages with #{chat.others.to_a[0]}"
end