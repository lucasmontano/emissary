# Emissary

A simple way to bind and exchange messages between activities and services.

#What emissary do for you
- Perform interprocess communication (IPC) using Messenger.
- Implements the Handler, where the service receives the incoming Message and decides what to do.

#Activity Implementation

Declaring the emissary

```java

    private Emissary.IEmissary emissary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        emissary = Emissary.getInstance(this);
    }
```

Bind a service with emissary connection

```java
  Intent intent = new Intent(this, WithLibService.class);
  bindService(intent, emissary.getServiceConnection(), Service.BIND_AUTO_CREATE);
```

Subscribe to a event
```java
  emissary.subscribe(WithLibService.ON_CHANGE_TIME_ZONE, new Emissary.EmissaryMessengerCallback() {

      @Override
      public void data(Bundle data) {
          ((TextView) findViewById(R.id.time_zone)).setText(data.getString(WithLibService.ARG_TIME_ZONE));
      }
  });
```

Request something to service
```java
  emissary.request(WithLibService.GET_TIME_ZONE, new Emissary.EmissaryMessengerCallback() {

    @Override
    public void data(Bundle data) {
        Toast.makeText(WithLibActivity.this, data.getString(WithLibService.ARG_TIME_ZONE), Toast.LENGTH_SHORT).show();
    }
  });
```

#Service Implementation

Declaring the Emissary
```java
  private final Emissary.IEmissary emissary = Emissary.getInstance(this);
```

Binding
```java
  @Override
  public IBinder onBind(Intent intent) {
      return emissary.getIBinder();
  }
```

Subscribe to respond activity's request
```java
  public void onCreate() {
      super.onCreate();

      emissary.subscribe(GET_TIME_ZONE, new Emissary.EmissaryMessengerCallback() {
          @Override
          public void data(Bundle data) {
              String timeZone = TimeZone.getDefault().getDisplayName();
              Bundle bundle = new Bundle();
              bundle.putString(WithLibService.ARG_TIME_ZONE, timeZone);
              emissary.send(GET_TIME_ZONE, bundle);
          }
      });
  }
```

Send a Event to Subscribers
```java
  String timeZone = TimeZone.getDefault().getDisplayName();
  Bundle bundle = new Bundle();
  bundle.putString(WithLibService.ARG_TIME_ZONE, timeZone);
  emissary.send(ON_CHANGE_TIME_ZONE, bundle);
```

[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-emissary-brightgreen.svg?style=flat)](http://android-arsenal.com/details/1/1836)
