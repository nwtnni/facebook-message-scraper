class Message
  attr_reader :author, :text, :time

  def initialize(data)
    @author = data.css('.user')[0]&.text
    @text = data&.next&.text
    @time = Time.parse(data.css('.meta')[0]&.text)
  end

  def to_s
    "#{ @time.strftime '%D %I:%M %p' } #{ @author }: #{ @text }"
  end
end