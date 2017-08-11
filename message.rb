class Message
  attr_reader :text, :time

	def initialize(data)
		@text = data.css('p')
		@time = Time.parse(page.css('.meta')[0].text)
	end
end