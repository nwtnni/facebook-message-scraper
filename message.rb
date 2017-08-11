class Message
	def initialize(data)
		@content = data.css('p')
		@time = Time.parse(page.css('.meta')[0].text)
	end
end