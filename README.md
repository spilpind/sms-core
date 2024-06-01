# SMS Core

This library serves as a common ground for core components of [Stick](#what-is-stick) Manager System (SMS).

The idea behind the library is in general to give clients and backend(s) the same understanding of for instance models,
contracts and logic used when dealing with SMS and to make it easier for both parties to implement.

The library are made with [Kotlin Multiplatform](https://kotlinlang.org/docs/multiplatform.html) and currently supports
JVM, iOS and nodeJS. More could easily be added (but hasn't been needed so far) - so let us know if you would like them
to be added.

All classes and functions are documented to give an understanding of what they each do or represent, but an overall idea
of the concepts can be found below.

## Concepts

### Models

The models do (surprise, surprise) define most of the entities used by system.

Each model usually have a raw part that only contains (close to) primitive properties which is useful when persisting or
transmitting the model (without having to transmit too much data).

On top of that most models also have a more detailed implementations which can come in handy when using the model in an
application or service. These implementations for instance reference other models (instead of just the ids like the raw
model would do) and could also include other data that usually isn't needed for the bare minimum raw model.

### API

The API contracts is originally intended to be used for websockets, which might be indicated a bit by how they are
structured. The API follows a special protocol which is structured by contexts that have actions (used for requests) and
reactions (used for responses).

#### Actions and Reactions

Actions and reactions are used for sending requests and getting responses. All actions have a corresponding reaction
(for instance action `add` has a reaction `added`), but some reactions might be used for several actions (like the
`updated` response). Some actions might result in several reactions, when for instance subscribing with the action
`subscribe`.

All actions and reactions are associated with some context, to both group and generalize them (such that for instance
`add` can be used for several contexts). More on that in the context-section below.

#### Contexts

A context defines a domain where actions and reactions can be used. The contexts do in most cases correspond to a
model - for instance `user`, `team` and `game`. There are however also expcetions, like `system` and `authorization`.

The context can be used by clients and services to easily figure out where a certain request or response belong and let
a data sources figure out what to do with that new info.

An alternative to contexts could be to add more info to each action, but that might lead to inconsistencies and
potentially similar looking actions. Instead of having actions like `teamAdd` and `gameAdd` (or `addTeam` and `addGame`)
and reactions like `teamAdded` and `gameAdded`, we now simply have the contexts `team` and `game` that each have a `add`
action and `added` reaction.

#### Why a new protocol? (And a bit of history)

The first iteration of SMS (called something else back then) was created in 2012-ish. It was a simple website with pages
that updated the entire page each time a request was made, as it was all done with POST calls. The second edition (still
not called SMS) was made in 2017-ish and now had asynchronous communication with the backend such that the entire
webpage didn't have to refresh on each update.

After a few years we started looking into making a new system (that was going to be called SMS!). We now wanted it to be
an app instead of just a website, but also wanted to solve an issue we've had with the previous systems: As we didn't
have any idea when new information was available (for instance for info-screens showing the current score), we had to
pull every X seconds - which both added unnecessary load to the servers and even still the information was potentially
not up-to-date until next refresh. Therefore, we started looking into websockets to enable up-to-date information at all
times - both for spectators and for referees.

There might be great protocols for solving the cases needed for SMS, but when we as two software developers
([anigif](https://github.com/Anigif) and [gyde](https://github.com/gyde)) initially needed to figure out a solution, we
thought it would be fun to design and implement a new protocol on our own. After a lot of discussions we ended up with
this design and liked the wording of actions and reactions (instead of the regular request/response). And trust us, we
also thought about making puns over the word "stick" - but that might have become too much of an inside joke.

### Permissions

SMS Core also contains logic that helps determine what functionality of the app the users (including the ones not logged
in) are allowed to do and access.

The logic mainly centers around the model `UserRole`. A user role defines a single role for a user in a given context.
The context here isn't the same as the one used for the API, even though they often overlap.

On top of this the user role specifies a context id which in most cases defines what item this user role is related to -
for a game, this would be the same as a game id, for team it's team id and so on. The exception to this is for instance
the system context, where the context id can be ignored (for now at least).

Note that a user can have several user roles for a given context/context id combination. A user could for instance
theoretically be both a member and captain of a team.

All this logic related to permissions might usually just have been a part of the client and services separately, but
each implementation would have their own understanding of the ruleset and potentially lack some information leading to
the users missing access to some functionality - or even show some functionality they shouldn't be allowed to access
(and doesn't find out about until the server reaction is negative).

## What is Stick?

So, the elephant in the room: What's Stick? It's a game played all over the world, but in a lot of variations, for
instance one called [tip-cat](https://en.wikipedia.org/wiki/Tip-cat). The variation this library supports is refined by
Danish Stick Association (in Danish: _Dansk Pind Union_). In Danish, Stick is called _Pind_.

The game consists of two teams that in turns are "in" and "out". Whenever a team is in they can get points, but if they
get too many deaths the two teams switch and the in team becomes the out team. The equipment needed to play is a small
stick, a long stick and a fence (for instance two bricks).

A more thorough guide with illustrations can be found in Danish at https://spilpind.dk/spillet.

## Disclaimer

This library has initially been developed for the main purpose of sharing the same logic between the Stick app and SMS
backend. It should be possible to use it for other apps or backends as well, but we can't promise that it will work in
all cases. We'll however do our best to make it work, so please reach out to us if you start using it - that will also
be fun to know!
