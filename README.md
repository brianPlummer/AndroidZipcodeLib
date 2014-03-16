This is a simple library that resolves zip codes to the appropriate City/State.  This can used for onboarding and ecommerce to help improve user experience by reducing the number of fields that the user must enter.  The memory footprint to include this library is around ~730KB.

There is a sample application that is included that illustrates the basic use of this library.  

ZipcodeLib.getCitiesAndStateThenDestroy()       -- returns results synchronously, don't call on UI thread

ZipcodeLib.getCitiesAndStateAsyncThenDestroy()  -- returns results asynchronously and returns the results via a passed                                                    in listener.

Advanced usage:

The library contains a compressed zip code database that is extracted and queried against.  The above functions getCitiesAndStateThenDestroy() and getCitiesAndStateAsyncThenDestroy() destroy the extracted database once queried. The uncompressed database is around 1.4MB and combined with the compressed version causes a footprint of around 2.1MB.  The library also allows you to control this and explicitly initialize and destroy the database if you wish to query it multiple times without destroying it.

ZipcodeLib.getCitiesAndState() and ZipcodeLib.getCitiesAndStateAsync() will both leave the extracted database intact after querying.  If the database is not extracted then any of the above methods will do this automatically.

ZipcodeLib.init()     --  extract the database upfront manually

ZipcodeLib.destroy()  --  deletes the extracted database

