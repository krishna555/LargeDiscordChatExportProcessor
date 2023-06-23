# Overview

1. This package is to be used with DiscordChatExporter to build a smaller view containing only fields of interest for each discord message.
2. The chat export JSON generated by DiscordChatExporter can be often very large for highly active Discord Channels. (Ex: #general or #general-chat on Discord servers). These channels may contain more than 20 Million messages during the channel's lifetime.
3. We are essentially seeking a solution to dive into a large nested JSON and iterate through each Discord message by retaining only a single message in-memory at a time. This package uses GSON to stream and process Discord Message objects generated by DiscordChatExporter.


# Installation.

1. Setup [DiscordChatExporter](https://github.com/Tyrrrz/DiscordChatExporter) CLI on your machine.
2. We will essentially try to get the entire chat history for a channel. Then, we will use python scripts to filter out messages relevant to keywords of interest.
3. To get the entire chat history of a channel use the following command of DiscordChatExporter.

`dotnet <INSTALLATION_PATH>/DiscordChatExporter.Cli.dll export -c <CHANNEL_ID> -o <OUTPUT_PATH> -f Json -t <DISCORD_USER_TOKEN>`
   1. Here, <CHANNEL_ID> is the Discord Channel Id in a URL of the form `https://discord.com/channels/<SERVER_ID>/<CHANNEL_ID>`.
   2. <DISCORD_USER_TOKEN> is the token associated with your Discord account. To retrieve this token you may follow the steps mentioned in the [DiscordChatExporter Docs](https://github.com/Tyrrrz/DiscordChatExporter/blob/master/.docs/Token-and-IDs.md#in-chrome).
   3. <OUTPUT_PATH> is the path where the chat history of the Discord Channel will be logged to.
**Note**: [Automating user accounts violates Discord's terms of service](https://support.discord.com/hc/en-us/articles/115002192352-Automated-user-accounts-self-bots-) and may result in account termination. Use at your own risk.
4. Now, we will create a minified view of every message containing only fields that are relevant to our task. Our script in this package currently generates the following minified metadata from each message representation of DiscordChatExporter.

```
[{
	"id": "(String) Message Id",
	"content": "(String) Message",
	"author": {
		"name": "(String) User Name",
		"id": "(String) User Id"
	},
	"pinned": "(Boolean) Was the message Pinned? (True or False).",
	"ts": "(String) Timestamp",
	"mentions": [{
		"id": "(String) Message Id referenced in the current message",
		"name": "(String) User name of the message referenced in the current message",
		"discriminator": "(String) Discord User Id ex: abcd#1234 implies discriminator is 1234",
		"nickname": "(String) User name alias of the Discord User",
		"color": "(String) Color",
		"isBot": "(String) Boolean is the the Discord User a Bot",
		"avatarUrl": "(String) URL of the Discord Profile User"
	}],
	"reactions": [{
		"emoji": {
			"name": "(String) Reaction Name",
			"id": "(String) Reaction Id",
			"code": "(String) Reaction Code",
			"imageUrl": "(String) URL containing emoji",
			"animated": "(String) Boolean is the emoji animated"
		},	
		"count": "(Integer) Number of users who upvoted the reaction."
	}],
	"attachments": [{
		"url": "(String) URL of attached links in the message",
		"fileName": "(String) File name of the URL ex: file:///a/b/c/d.json implies file name is d.json"
	}]
}]
```
5. Clone the Github repository [LargeDiscordChatExportProcessor](https://github.com/krishna555/LargeDiscordChatExportProcessor) into your local machine.
6. Setup the maven project in your IDE of choice (Ex: Intellij Idea).
7. The pom.xml uses JDK 19. You may update that setting based on the JDK of your choice.
8. Make sure to update keywords.txt in `src/resources` with a keyword in each line. The program will only select messages containing one of the keywords.
9. We need to run DiscordChatAggregator. Setup the arguments for the program as:
<INPUT_PATH> <OUTPUT_PATH_1> <OUTPUT_PATH2>
Here, 

   1. <INPUT_PATH> is the path to the JSON generated by DiscordChatExporter
   2. <OUTPUT_PATH_1> is a raw dump of the messages post keyword-filtering.
   3. <OUTPUT_PATH_2> is the **result** of the program's execution containing a map with key as the keyword and value as the list of message objects containing the keyword.