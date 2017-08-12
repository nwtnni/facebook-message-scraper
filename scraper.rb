require 'nokogiri'
require 'time'
require_relative 'chat'

class Scraper

  attr_reader :user

  def initialize(file)  
    html = Nokogiri::HTML(open file)  
    @user = html.css('h1')[0].text
    @chats = html.css('.thread').map { |chat| Chat.new(chat, @user)}
    @chats.select! { |chat| !(chat.missing? || chat.group?) }
  end

  def search(name)
    @chats.select { |chat| chat.others.include? name }.reduce { |a, b| a + b }
  end

  def others
    Set.new(@chats.map { |chat| chat.others })
  end
end