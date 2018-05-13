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
    ircChannels=#firstchannel,#secondchannel
    botOwner=nickname
    botOwnerMask=nickname!username@host
    botOwnerPassword=
    botUsers=nickname1,nickname2
    badWords=bad,words,go,here
    dbKnowledge=knowledge.db
    dbUrl=url.db

The properties `ircChannels` and `botUsers` are comma delimited.

To set the `botOwnerPassword`, send a private message with `setpass <password>`. Make sure you set the `botOwner` and `botOwnerMask` property when running the bot for the first time.

# Basic commands
Command all IRC users may use.

#### Query a topic

    ?<topic>

Example:

    <exampleNick> ?sometopic
        <jIRCBot> Here's what I know about sometopic: This is some text about the topic

#### 8 Ball

    !8ball <optional question>

Example:

    <exampleNick> !8ball Do I feel lucky?
        <jIRCBot> It is certain
    
#### BOFH

    !bofh
    
Example:

    <exampleNick> !bofh
        <jIRCBot> Multiplexed Stack Dereferencing Signal
    
#### Flip a coin

    !flipcoin
    
Example:

    <exampleNick> !flipcoin
        <jIRCBot> It's heads

# Know user commands
Commands known IRC users can use. Users can be added and removed using the `!adduser` and `!deluser` owner commands.

### Add knowledge

    !learn <topic> <topic text>
    
Example:

    <exampleNick> !learn sometopic This is some text about the topic
        <jIRCBot> OK exampleNick, I now know about sometopic.
    
### Forget knowledge

    !forget <topic>
    
Example:

    <exampleNick> !forget sometopic 
        <jIRCBot> OK exampleNick, I forgot about sometopic.

# Bot owner commands
Bot owner commands only work via PRIVMSG and always requires the first argument to be the owner password.

### Authenticate

    auth <password>

### Set owner password

    setpass <password>
    
### Add a user

    adduser <user>
      
### Delete a user

    deluser <user>
    
### List users

    listusers
    
### List bad words

    listwords
