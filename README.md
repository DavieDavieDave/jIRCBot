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
    ircJoinOnKick=false
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

### Query a topic

    ?<topic>

Example:

    <exampleNick> ?sometopic
        <jIRCBot> [sometopic] This is some text about the topic

### Knowledge index

    !index
    
Example:

    <exampleNick> !index
        <jIRCBot> Index: topic1, topic2, topic3
        
### 8 Ball

    !8ball <optional question>

Example:

    <exampleNick> !8ball Do I feel lucky?
        <jIRCBot> It is certain
    
### BOFH

    !bofh
    
Example:

    <exampleNick> !bofh
        <jIRCBot> Multiplexed Stack Dereferencing Signal
    
### Flip a coin

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
Bot owner commands only work via PRIVMSG. The bot onwer must be authenticated to use these commands.

### Authenticate

    auth <password>

Authenticates the owner. This will update the user nick and hostmark in the `config.properties` file.

Example:

    <exampleNick> auth password
        <jIRCBot> Authenticated

### Set owner password

    setpass <password>

Sets the owner password.
    
Example:

    <exampleNick> setpass password
        <jIRCBot> Password set    

### Add a user

    adduser <user>

Add a known user.

Example:

    <exampleNick> adduser otherNick
        <jIRCBot> User added    

### Delete a user

    deluser <user>

Removes a known user.

Example:

    <exampleNick> deluser otherNick
        <jIRCBot> User deleted    

### List users

    listusers

Returns a list of known users.

Example:

    <exampleNick> listusers
        <jIRCBot> exampleNick, otherNick   

### Add bad word

    addword <word>

Add a bad word.

Example:

    <exampleNick> addword badword
        <jIRCBot> Word added   

### Delete bad word

    delword <word>

Removes a bad word.
    
Example:

    <exampleNick> delword badword
        <jIRCBot> Word deleted   

### List bad words

    listwords

Returns a list of bad words
    
Example:

    <exampleNick> listwords
        <jIRCBot> badword, notniceword, otherword   

### Join a channel

    join #channel

Causes the bot to join a channel

Example:

    <exampleNick> join ##other_channel
        <jIRCBot> Joining channel ##other_channel   

### Leave a channel

    part #channel

Causes the bot to leave a channel
    
Example:

    <exampleNick> part ##other_channel
        <jIRCBot> Leaving channel ##other_channel   

### List channels

    listchannels

Returns a list of channels the bot is joined with channel modes.
    
Example:

    <exampleNick> listcannels 
        <jIRCBot> Channels: ##bot_channel [+nt], ##other_channel [+Ccgnt]