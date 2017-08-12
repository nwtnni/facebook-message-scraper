# facebook-message-scraper
Ruby program that uses [Nokogiri](https://github.com/sparklemotion/nokogiri) to extract
individual chats from raw Facebook data.

## Motivation

Facebook Messenger doesn't allow users to access old chat messages: scrolling up
soon becomes impractical, and the search function can be unreliable. I wanted to
take the raw data and parse it into a simple text file, which is much easier to
navigate.

## Features

- See the conversation length of your 25 longest non-group chats
- Extract individual chats with friends into text files

## Instructions

Facebook allows users to download a copy of their account data. This link can be
found under General Account Settings (it's easy to miss). After following the
directions and clicking through a few e-mails, you should have a `facebook.zip`
folder. After unzipping, you can find the message file under `facebook/html/messages.htm`.

## Usage

`ruby main.rb <MESSAGE_FILE>`

It will take some time to parse through the data; the program will automatically print
out the length of your non-group chats, and then start an interactive prompt where
you can enter in individual friends. For example,

`John Smith john_smith.txt`

will find your chat with `John Smith` (if it exists) and write all of the messages to `john_smith.txt`.