
# Devrev Network SDK

This is a sample with integration of devrev network sdk.


## Getting Started

- Clone this repo:

```sh  
git clone https://github.com/atiqulalam/devrev.git  
  
- Replace BASE_URL and API_KEY with your url and key in gradle file.  
- Compile and build the app 
  
Build NetworkClient and configure the object
val netwrokClient = NetworkClient.Builder()  
    .setBaseUrl(BuildConfig.BASE_URL)  
    .addHeader("Accept", "application/json")  
    .addHeader("Content-Type", "application/json")//Content-Type: text/html  
    .addHeader("User-Agent", userAgent)  
    .setConnectionTimeout(10) // seconds  
    .setReadTimeout(10) // seconds  
    .enableLogging(true)  
    .setConverterFactory(converterFactory)  
    .build()
**GET call**
networkClient.get("/3/movie/$movieId",  
    queryParams = mapOf("language" to "en-US","api_key" to BuildConfig.API_KEY),  
    callback = object : NetworkCallback {  
        override fun onSuccess(response: NetworkResponse) {  
            val post = networkClient.parseResponse(response.body, MovieDetail::class.java)  
            //parse custom api response
  }  
  
        override fun onFailure(error: Throwable) {  
           // error handling
  }  
    })
**POST call**

networkClient.post("/3/movie/$movieId",MovieDetail(),  
    queryParams = mapOf("language" to "en-US","api_key" to BuildConfig.API_KEY),  
    callback = object : NetworkCallback {  
        override fun onSuccess(response: NetworkResponse) {  
            val post = networkClient.parseResponse(response.body, MovieDetail::class.java)  
                  //parse custom api response
  }  
  
        override fun onFailure(error: Throwable) {  
         // error handling
  }  
    })