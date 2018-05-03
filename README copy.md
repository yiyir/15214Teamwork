The framework analyzes data on restaurants from a given city.

It gets data from the DataPlugin, which currently supports Yelp local database and Google Places API, sort or filter the data based on the client's demand, and display the results through DisplayPlugin, which currently supports bar-charts and pie-charts.

To implement the DataPlugin, a class only needs to implement the getRstaurants method, which takes a city name as a String and returns a list of Restaurant, which is a type defined in the framework tailored to the domain.

To implement the DisplayPlugin, a class only needs to implement the displayMethod, which takes the parameters of an arbitrary graph, and display the graph in a new Java Swing window.

A Restaurant must have a name, a city, and opening hours to be qualified as a restaurant, thus restricting the domain of the framework. At the same time a Restaurant stores a List of keys and values, which can be any data of type String and Double approproate for the data source defined in the corresponding DataPlugin, thus making the framework extensible.

To run new plugins with the framework, you just need to implement the DataPlugin interface or DisplayPlugin interface, and include dependency in the plugin project. Also list out all the classes there in the 'resources' files.


Overall, the domain you choose is interesting and demonstrated a careful thought process of what responsibility to assign to which component. But we have some suggestions or questions regarding to design of your data plugin and how it interacts with your framework.

Suggestions:

The design of your data plugin interface could be improved. Representing data as 2D `string` array might not be a good choice. It is OK (has nothing to do with information hiding) to define a data structure as the input of your framework for your user. You might want to use `Restaurant` class to define your input data.
Your visualization plugin takes in data as a list of generic type. This might not be a good idea for visualizing the data because for histogram, bar char and scatter plot, you need some numeric data of type (data of type `int`, `double`, etc.). You might consider defining your input data as a list of type`int` or `double` instead of doing type-checking when you want to plot your data. 
From your documentation, it is not clear what functionality your framework provides. You might want to do simple data processing/analysis on your input data, e.g. sort, filter, etc.
Other comments:
Your presentation is clear and easy to follow. Nice job!
Overall, the domain and extensibility of the framework looks good. Thanks for your presentation! If you have any questions about feedback, feel free to email either of us or post privately on Piazza.
