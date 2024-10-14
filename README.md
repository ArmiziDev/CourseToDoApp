# Course To-Do App
This Android app is designed to help users manage and track to-do items for their courses. The app supports the creation, editing, deletion, and display of multiple to-do items. Each item is comprised of a course ID, title, description, and last updated time. This project showcases skills in Android development, including RecyclerView, multiple activities, JSON file handling, and option menus.

## Features
RecyclerView: To display the list of to-do items.
Multi-Activity: App consists of three main activities: Main Activity, Edit Activity, and About Activity.
JSON File Handling: To-do items are saved to and loaded from the internal storage in JSON format.
Options Menu: For adding new items, accessing app information, and more.
Persistence: To-do items are loaded upon app launch and saved after modifications.
Screenshots
(Include screenshots of Main Activity, Edit Activity, and About Activity here)

## Technical Summary
1. Main Activity
Displays all to-do items in a RecyclerView, sorted by the latest update time.
Option menu for adding new items or viewing app information.
Long-pressing an item triggers a confirmation dialog to delete the selected to-do.
UI design adheres to best practices: distinct background color, bold course ID, title, and truncated description with "..." when exceeding 80 characters.
2. Edit Activity
Users can create new to-do items or edit existing ones.
Editable fields include course ID, title, and description.
"Save" button in the options menu to update the list in Main Activity.
Handles validation (course ID and title required) with user-friendly feedback through Toast messages and dialogs.
3. About Activity
Displays the app’s title, version, author, and a copyright date over a full-screen image background.
No interactive functionality aside from the Back button to return to Main Activity.
## Key Concepts & Techniques
1. RecyclerView
Implemented to handle dynamic lists with efficient scrolling.
Custom AssignmentAdapter was created to bind data to the UI.
Efficient data updates using notifyDataSetChanged() after adding or deleting items.
2. JSON File Handling
Loaded data from a JSON file using internal storage in onCreate().
Saved data back to the JSON file when items were added or removed.
Handled cases where the JSON file was not found at launch, ensuring a smooth user experience.
3. Activities and Intents
Used multiple activities (MainActivity, EditActivity, and AboutActivity) to break up the app into logical components.
Intent was used for navigation between activities.
Implemented data passing between MainActivity and EditActivity using Intent.putExtra() and Intent.getExtra().
4. Options Menu
Added custom icons to the app bar for adding new items, viewing the About page, and saving items.
Handled user input and menu item selection in onOptionsItemSelected().
5. Dialogs and Toasts
Confirmation dialog for deleting items.
Alerts for unsaved changes when exiting the Edit Activity without saving.
Toast messages for user feedback such as "Empty item not saved" and "Cannot save item without course ID and title."
## Lessons Learned
Working with RecyclerView: Understanding the importance of ViewHolders and Adapters in efficiently managing list views.
Activity Lifecycle: Managing data loading and saving at the right points in the activity lifecycle (e.g., onCreate() and onPause()).
File Handling in Android: Implementing persistent storage using JSON files for storing and retrieving data.
User Experience: Ensuring that the app is intuitive and provides clear feedback to users through dialogs and toasts.
## How to Run
Clone this repository.
Open the project in Android Studio.
Build and run the app on an Android device or emulator (API level 28 or higher).
