# StockSock
##### (You know, because it's about stocks.... and it's using websockets... ok moving on)

First let me start by stating the obvious, this project is massively over-engineered for the task.  By architecting in the way I have, I hope to deomonstrate how a larger scale project might scale in predictable ways.  It's my belief that a good architecture not only helps deliver on the SOLID principles but also helps a developer write better code faster.

Model-View-Intent (MVI) has been gaining a lot of steam in the Android community in the last few years.  Starting with [Hannes Dorfmann](http://hannesdorfmann.com/android/model-view-intent), MVI on Android was taking the best aspects of web programming and translating them for mobile.  This movement jumped in popularity after [Jake Wharton's RxJava state video](https://www.youtube.com/watch?v=0IKHxjkgop4) and has been gaining ground ever since.

The concept of MVI or a "Uni-directional data flow" architecture is simple enough.  You have a ***View*** which is passive - this is a trait shared with most other popular MV* architectures with the notable exception of MVC and monolithic Activies (we all know how those work out :P).  The ***Intent*** is the *what*.  What do you want to do?  What's your intent.  Confusingly, this shares both a name and general concept with the Android Intent class but they are distinct ideas.  The Intent in MVI is usually represented by a sealed class to neatly express all possible outcomes.  Finally the ***Model***, a representation of the *State* of your app at any point in time.  This is the crux of this pattern and what gives it more expressive power than any other popular architecture on Android.  Imagine knowing exactly the state of your application when there was a crash in prod.

### Carte Blanche

As with all things theoretical, there are endless options for implementation that generally fit the bill.  There have been *many* libraries developed both by large companies (Airbnb, Groupon) and individuals trying to promote their variants.  However, with the power of Kotlin I see no reason to lock yourself down to a 3rd party library and in fact, much more flexibility can be achieved by using some simple language concepts and a reactive framework.  Of course I've made subjective choices for this project but I hope to convince you of their merit below.

The components I chose as my building blocks are defined here:

- ***Template***
  - A ViewGroup subclass.  Pure UI.  The template has only two jobs in the entire world: render some State it has been given and telling other components about user input events that have happened.
- ***View***
  - That Template has to go somewhere - it goes in the View.  The View is a Fragment or Activity that hosts the Template.  Its job is to ferry data to and from the template.  This is also where the connection logic to the viewmodel happens.
- ***Event***
  - Those user actions from the Template?  They are abstracted as Events.  An Event defines a user action and it contains all the data further steps might need to act on that action.  For example, a login Event would probably be a data class that contains the username and password on the screen so further steps can validate that data and do something to log the user in.
- ***UseCase***
  - Now we're in the meat of it.  This is pure business logic that takes an Event and decides how to turn that into a function that modifies the view state.
- ***Action***
  - The Action IS the function that modifies the viewstate.  Literally its input is the OLD viewstate and its output is the new.  Input/Output - remember that.  We want [PURE FUNCTIONS](https://en.wikipedia.org/wiki/Pure_function)
- ***ViewState***
  - Is a loading spinner showing?  Is there a list of Sprockets in a Recyclerview?  Whatever is on the screen is defined explicitly in the ViewState for that feature.
- ***ViewModel***
  - Something has to glue all of this stuff together.  The entire stream thus far defined: Event -> Use Case -> Action, needs to happen in one continuous stream (Observable in this case).  All of this is handled in the ViewModel which is called Store in this app.
- ***Service***
  - General Service pattern.  Services abstract network interfaces usually in the form of Retrofit clients
- ***Converters***
  - Using network models all throughout your app is a big no-no.  Once we grab some data from the network we use converters to map it into our more appropriate and null-safe domain models.

### Some Notes on Library Choices

- ***Scarlett***: Working with websockets can be a real pain since it's a low level protocol.  Five years ago it was like using raw C sockets but our friends (more than friends?) over at Tinder have provided an interface abstraction on top of OkHttp that very closely mimics good old Retrofit.  You'll notice the interface uses Flowables due to the explicit backpressure handling and you'll notice I abandon that safety net very quickly and convert to an Observable.  The WSS provided does not emit fast enough to immediately cause backpressure issues and since this is a demo app, Observables were easier to work with.
- ***ToothPick***: Toothpick is a scope based dependency injection framework that was developed by some engineers at Groupon.  It provides more flexibility than Dagger2 setup with Hilt and it's faster to boot.  Toothpick and its Kotlin bindings is the unsung hero of modern Android DI.
- ***RxJava***: The MVI pattern can just as easily be done with Kotlin Coroutines and Flow.  Doing so, however, requires the use of some beta APIs as well as some pretty gross stuff with Channels.  RxJava is simply a more elegant approach at this time.

The rest of the libraries I've chosen are pretty much on par with any other modern Android app.