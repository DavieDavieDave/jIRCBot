# jIRCBot
A Java IRC bot using PircBotX.

This Java IRC bot is under development. It utilizes PircBotX and knowledge data is stored in a SQLite database.

The bot has only been tested on Freenode. It requires SSL and SASL to connect, so make sure your bot nickname has been registered.

Create a config.properties file with the following:

    ircName=jIRCBot
    ircLogin=jIRCBot
    ircRealName=jIRCBot
    ircSASLPassword=NickServPassword
    ircServer=irc.freenode.net
    ircPort=6697
    ircChannels=##jircbot-test|##jircbot-test2
    botOwner=nickname
    botOwnerPassword=
    botUsers=nickname1|nickname2

The properties `ircChannels` and `botUsers` are delimited by `|`.
