The framework analyzes data on restaurants from a given city.

It gets data from the DataPlugin, which currently supports Yelp local database and Google Places API, sort or filter the data based on the client's demand, and display the results through DisplayPlugin, which currently supports bar-charts and pie-charts.

To implement the DataPlugin, a class only needs to implement the getRstaurants method, which takes a city name as a String and returns a list of Restaurant, which is a type defined in the framework tailored to the domain.

To implement the DisplayPlugin, a class only needs to implement the displayMethod, which takes the parameters of an arbitrary graph, and display the graph in a new Java Swing window.

A Restaurant must have a name, a city, and opening hours to be qualified as a restaurant, thus restricting the domain of the framework. At the same time a Restaurant stores a List of keys and values, which can be any data of type String and Double approproate for the data source defined in the corresponding DataPlugin, thus making the framework extensible.

To run new plugins with the framework, you just need to implement the DataPlugin interface or DisplayPlugin interface, and include dependency in the plugin project. Also list out all the classes there in the 'resources' files.
