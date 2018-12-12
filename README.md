# Arete

Our mission is to help our users feel that they can manage to get through their day much easier, and most importantly, feel better while doing so. This includes understanding daily progress through short-term and long-term goals, becoming more self-aware by reflecting upon daily self-awareness mental exercises, fostering good habits through the use of achieving daily tasks and goals, and ultimately becoming more positive.

## Team
Chad Catt, Veronica Day, Shawn Dong, Gustav Ale Svensson, Cassandra Wong

## Primary Features
### Morning Question

This morning question will allow the user to think about what would make this day good for him or her. It starts off by having the user answer the question "I will feel great about today if..."

#### Features

1. The answer for the morning question can be submitted by pressing the image view button of a pencil. Upon submission, the answer gets populated into the Firebase Database.
2. The answer can be edited and re-submitted.
3. The answer remains static for the rest of the day. When it turns to a new day, the user will be able to type in a new response which then gets populated into the Firebase Database for the respective day.

### Task Planner

This task planner allows the user to think about what tasks he or she would like to accomplish for the day.

#### Features

1. There is functionality for the user to add and delete tasks.
2. Upon completion, the user can reflect upon that task (ex. how completing the goal made him or her feel).
3. The tasks and their respective reflections are automatically populated into the reflection log.

### Calendar

The calendar allows the user to jump to different days that he or she has recorded, as well as view an overall look of how the days have been. 

#### Features

1. There is a "jump-to-date" button that allows the user to jump to dates that may not be easily accessible. All the dates follow a mm-dd-yyyy format.
2. The user is not able to view the reflection logs for future days, as those days have not occured yet. 
3. The user is able to view the reflection logs for past days, which takes in information stored in the Firebase Realtime.
4. There is a functionality added in so that the user is not able to edit the reflection log for past days.
5. The calendar has a functionality where the day turns to a different shade of teal corresponding to how the user felt for the day (given in the star rating functionality in the Reflection Log for the respective day). The darker the shade of teal, the better the user rated the day.

### Self-Awareness Mental Exercise

In the reflection log, there is a feature for the user to go through a list of self-awareness exercises. 

#### Features

1. We have populated around 138 in our code but there is the ability for more to be added. 
2. The mental exercises follow a "round-Robin" style, where the questions get populated in order. Effectively, with what we have in our code, the questions would repeat every 138 days.
3. Upon finishing typing in the text box for the mental exercise, the user input gets populated into the Firebase Database.

### Evening Reflection

This feature allows the user to reflect on the day as a whole, and give it a ranking from 1-5 which will then be implemented in the Calendar so the user can look back on the past days that he or she has recorded and see how the days have been.

### Log Out

This button on the bottom navigation bar allows the user to Log Out of the application.
