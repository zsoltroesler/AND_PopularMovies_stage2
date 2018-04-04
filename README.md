# AND_PopularMovies_stage2
Udacity <a href="https://eu.udacity.com/course/android-developer-nanodegree-by-google--nd801/" 
       title="Android Developer Nanodegree">Android Developer Nanodegree</a> by Google project#2, final stage of Popular Movies app

**Project #2: Popular Movies app**

Project overview: In this second and final stage, the app has functionality more than Stage 1:
- allows users to view and play trailers ( either in the youtube app or a web browser).
- allows users to read reviews of a selected movie.
- also allows users to mark a movie as a favorite in the details view by tapping a button(star).
- creates a database and content provider to store the names and ids of the user's favorite movies (and optionally, 
the rest of the information needed to display their favorites collection while offline).
- modified the existing sorting criteria for the main view to include an additional pivot to show their favorites collection.
+ additional functions like searching in themoviedb.org database, refresh data, sharing the film details and sorting by "now playing" movies
 
 Tags: JSON parsing, HTTP networking, AsyncTaskLoader, RecyclerView, ViewHolder, CardView, Picasso, ButterKnife, ContentProvider, SQLite database
 
![popularmovies2](https://user-images.githubusercontent.com/23049871/38334763-8fa6736e-385c-11e8-876e-49ca274717de.gif)

Note: API key to fetch movie data from themoviedb.org website was removed from gradle.properties file due to sensitive data. In order to use this open repo you should request an API key from themoviedb.org
