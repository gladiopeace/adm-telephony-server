# Introduction #

VoIP (Voice Over Internet Protocol) is the umbrella of protocols used to transmit voice (or video) over data networks.

Mixing data and voice, or considering voice as data in a special format, opened the door for applications that were not available in the pure PSTN (Public Switched Telephone Network).


Nowadays, most VoIP switches are computer based, with Linux as the operating system of choice.

Although there are plenty of available open-source VoIP switches, ASTERSIK was for a long time the de-facto switch for open-source low density switches.

Another late-comer is FreeSwitch, which has learned from ASTERSIK and was build as a high capacity, well designed Soft-Switch.

ADM Telephony Server (ATS) makes use of ASTERISK and FreeSwitch to offer a distributed application server.


# Why ADM Telephony Server (ATS) #
Although both switches (ASTERISK and FreeSwitch) offer an API to develop call management applications (like voicemail, follow-me, conference …), they are limited in terms of programming model, application architecture, and API.

## Architecture limitations ##
The model followed by both switches relies on in-process architecture to run the applications and scripts. While this offers a fast access to the application to the core API, it limits the distributability and redundancy of the whole system.

## Programming model ##
Both switches offer an synchronous API. Programming telephony applications ,which are event driven by nature, using an synchronous call logic, limits the application's scalability. Each call need will need at least one thread. Having 200 simultaneous calls for example, will require at least 200 threads, this will add additional overhead to the OS as thread context switching will take considerable system time.

## API limitations ##
ASTERISK offers access to its core functionality using AGI (ASTERISK GATEWAY INTERFACE). This API is not coherent and missing some core functionality (like  collecting DTMF and playing files)
FreeSwitch offers a better API for interacting with the core functions. (TBD)

ATS tries to remedy those problems by offering a coherent API and a distributed and scriptable environment to create applications. It uses the Manager Interface for ASTERISK and ESL (Event Socket Library) for FreeSwitch.
It is written in Java and can be extended by writing scripts in Java or Groovy.


# ATS benefits #
  1. Multithreaded asynchronous telephony engine.
  1. Uses Asterisk or FreeSwitch for media and call control.
  1. Leverage your current telephony infrastructure.
  1. Develop distributed load balanced telephony applications
  1. Powerful API : Enhances each switch’s API by providing a more coherent and unified interface.
  1. Powerful scripting engine: you can writer script in Java or Groovy.


# ATS features #

## Distributed architecture ##
ATS can be deployed on a standalone machine or on the same machine as the switch. It can connect to multiple switches (ASTERISK or FreeSwitch) offering a single switch appearance to the application.

## Unified asynchronous API ##
ATS offers a unified asynchronous API for both ASTERISK and FreeSwitch.
Example of an API call:
> channel.playAndGetDigits(10, "callingcard/accountnum", 10000, "#")

## Plugable architecture ##
User can modify the behavior of the ATS by providing his own components.
Example of pluggable components are:
  1. Prompts builders.
  1. Script builders (code that finds the appropriate script to handle a call).
  1. Event listeners (add components that can listen to system wide events)
All components can be developed by either Java or Groovy.

## Hot-Swap scripting engine ##
Scripts developed using groovy can be added to the server without restarting.

## Radius (AAA) enabled ##
ATS can act as a Radius client. The client implements authorization and accounting messages. Start and stop accounting as well as accounting interim update.

## Prompt builders ##
One of the major problems in IVR systems is playing dates, times, numbers and digits in different languages.
The problem is particularly difficult for numbers, since the algorithms to transform the numbers to sentences differ greatly from one language to another.
Another problem presented in our case (abstracting the Asterisk and FreeSwitch switches), is the fact that each switch deals differently with the prompts presented.
In order to solve this problem, we have elected to designing our own prompt system, the prompt builder.

## REST API ##
Interact with the server using REST and JSON.


## Built-in web server ##
Create your own Servlets and use them to interact with the core server (Configure, monitor, control ...)