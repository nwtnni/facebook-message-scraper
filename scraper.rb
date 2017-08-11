require 'nokogiri'
require 'time'
require_relative 'chat'

class Scraper

  def initialize(html)
   @user = html.css('h1')[0].text
   @chats = html.css('.thread').map { |chat| Chat.new(chat, @user)}
  end


  
  # chats = chats.map { |chat| Chat.new(chat, user) }
  # chats = chats.select { |chat| !(chat.group? || chat.missing?) }
  # chats = chats.sort_by { |chat| chat.messages.length }
  # chats = chats.reverse

  # chats.each do |chat|
  #   puts "#{chat.messages.length} messages with #{chat.others.to_a[0]}"
  # end

end