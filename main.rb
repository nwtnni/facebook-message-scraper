require_relative 'scraper'

begin
  scraper = Scraper.new(ARGV[0])
rescue Exception => e
  puts "Error: invalid message file."
  puts "Usage: ruby main.rb <MESSAGE_FILE>"
  exit
end

puts "Your top 25 chats are:"
top = scraper.others.map { |name| scraper.search name.to_a[0] }.sort_by { |chat| chat.messages.length }
top.reverse.take(25).each { |chat| puts "#{chat.others.to_a[0]}: #{chat.messages.length} lines"}

def usage
  puts "\nEnter a command: <NAME> <OUTPUT>"
  puts "Where <NAME> is the other person to search for (e.g. John Smith)"
  puts "Where <OUTPUT> is the file to write the chat to (e.g. john_smith.txt)"
  puts "Or type q to quit\n"
end
usage

while true 
  input = $stdin.gets.chomp.split
  if input.nil? || input[0] == 'q'
    break
  end

  other = input[0..(input.length-2)].join(" ")
  output = input[-1]
  chat = scraper.search(other)

  if chat
    File.open(output, "w") { |file| chat.messages.each { |message| file.write(message.to_s + "\n") } }
    puts "Done writing. Enter another command!"
  else
    puts "Could not find conversation with #{other}."
    usage
  end
end