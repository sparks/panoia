Panioa
=======
A Paranoid Street-View library for Processing.

Description
-----------
Panoia follows the design of the Google Maps v3 API for StreetView. It provides access both to the [Static Tile API](https://developers.google.com/maps/documentation/streetview/) and to the underlying raw tiles that can be used to create arbitrarily large scenes just like the [Maps API](https://developers.google.com/maps/documentation/javascript/reference#StreetViewTileData). The static tiles have been corrected for the spherical projections while the raw tiles have not.

The library also sets out to provide a number of other useful features:
* Simple methods to project headings into the rendered scene
* Methods to project lat/lng points and other objects in to scene.
* Methods to draw over interesting features in the scene such as roads.
