require 'nokogiri'
require 'time'
require_relative 'chat'

class Scraper

  attr_reader :user

  def initialize(html)  
    @user = html.css('h1')[0].text
    @chats = html.css('.thread').map { |chat| Chat.new(chat, @user)}
    @chats.select! { |chat| !chat.missing? }
  end

  def search(name)
    @chats.select { |chat| chat.others.include? name }
  end
end