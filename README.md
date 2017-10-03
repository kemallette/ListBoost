ListBoost
=========
ListView and ExpandableListView are quite powerful and fundamental to most apps. Unfortunately, they are also the source for many headaches and face palms. I’m not ashamed to admit spending more than a couple evenings staring angrily at misbehaving lists and adapters. If you are familiar with ExpandableListView, you have probably noticed that it is missing some features (such as choice modes) compared to its parent, ListView. Additionally, you have probably noticed that both ListView and ExpandableListView can be a pain to implement for use cases that fall outside of using a simple adapter and built in list item views. The aim for ListBoost is to implement some much needed _new_ functionality as well as to add conveniences where possible without requiring users to implement complicated wrappers or extra boilerplate.

Major Features Roadmap
----

#### ExpandableListView
  - Sliding list item menus
  - Swipe-to-reveal menus
  - Drag and Drop
  - MultiChoice Modes

#### ListView
  - Sliding list item menus
  - Swipe-to-reveal menus
  - Drag and Drop

****

ChangeLog
---

#### ListBoost v0.2.0
  - MultiChoice functionality for BoostExpandableListView added
    TODO: saveInstanceState isn't implemented yet and checkChildrenWithGroup hasn't been tested yet
  - Demo project is functional for BoostExpandableListView (only choice mode functionality is available)

#### ListBoost v0.1.0
  - ListView sliding list item menus is working for both cursor and non-cursor adapters.
  - ExpandableList multiSelect mode is mostly scaffolded out.

****

### Pull Requests and Contributing
Feel free to create pull requests if you'd like to contribute or if you'd like to get more involved with ListBoost, shoot me an e-mail at kemallette@gmail.com.

### Acknowledgments and Thanks

  * Rocket by Chris Kerr from [The Noun Project](http://thenounproject.com/)

  * Some ListView sliding item menus functionality originally from [Tjerk Wolterink's][tjerk]
project, [Android-SlideExpandableListView][slide]
  * Some ListView Drag and drop functionality originally from [Bauerca's][dragdrop_author] project, [drag-sort-listview][dragdrop] (**no longer maintained**)
  * Some ListView swipe to reveal functionality originally from [47deg's][47] project, [android-swipelistview][swipe]

### License

```
Copyright 2017 Kyle Mallette

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
[slide]: https://github.com/tjerkw/Android-SlideExpandableListView
[tjerk]:https://github.com/tjerkw

[dragdrop]: https://github.com/bauerca/drag-sort-listview
[dragdrop_author]: https://github.com/bauerca

[swipe]: https://github.com/47deg/android-swipelistview
[47]: https://github.com/47deg
