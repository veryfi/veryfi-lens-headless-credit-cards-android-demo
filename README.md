<img src="https://user-images.githubusercontent.com/30125790/212157461-58bdc714-2f89-44c2-8e4d-d42bee74854e.png#gh-dark-mode-only" width="200">
<img src="https://user-images.githubusercontent.com/30125790/212157486-bfd08c5d-9337-4b78-be6f-230dc63838ba.png#gh-light-mode-only" width="200">

[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](https://opensource.org/licenses/MIT)

# Veryfi Lens

<a href="https://apps.apple.com/co/app/veryfi-lens/id1498300628?l=en"><img src="https://upload.wikimedia.org/wikipedia/commons/thumb/3/3c/Download_on_the_App_Store_Badge.svg/2560px-Download_on_the_App_Store_Badge.svg.png" width="180"></a>&nbsp;&nbsp;
<a href="https://play.google.com/store/apps/details?id=com.veryfi.lensdemo"><img src="https://en.logodownload.org/wp-content/uploads/2019/06/get-it-on-google-play-badge-1.png" width="180"></a>

Veryfi Lens is code (a framework) with UI for your mobile app to give it document capture superpowers in minutes.

Let Veryfi handle the complexities of frame processing, asset preprocessing, edge routing, and machine vision challenges in document capture. We have been at this for a long time and understand the intricate nature of mobile capture. That’s why we built Lens. Veryfi Lens is built by developers for developers; making the whole process of integrating Lens into your app fast and easy with as few lines as possible.

Veryfi Lens is a Framework: a self-contained, reusable chunks of code and resources you can import into you app.

Lens is built in native code and optimized for fast performance, clean user experience and low memory usage.

You can read further about Lens in Veryfi's dedicated page: https://www.veryfi.com/lens/

## Table of content
1. [Veryfi Lens Android Examples](#examples)
2. [How to add Veryfi Lens to your project](#maven)
3. [How to run this project](#configuration)
4. [Other platforms](#other_platforms)
5. [Get in contact with our team](#contact)

## Veryfi Lens Headless Credit Cards Android Example <a name="example"></a>
This is an example of how to use Veryfi Lens Headless Credit Cards in your app, you can find the developer documentation [here](https://app.veryfi.com/lens/docs/android/).

![headless_credit_card](headless-credit-cards-demo.gif)

## How to add Veryfi Lens Credit Cards to your project <a name="maven"></a>

Install from our private [Maven](https://nexus.veryfi.com/repository/maven-releases/), a
package manager for Java.

Add in your project build.gradle file the Veryfi Lens Headless Credit Cards android SDK dependency:
```ruby
dependencies {
    implementation 'com.veryfi.lens.headless:credit-cards-sdk:2.0.0.1'
}
```

## How to run this project <a name="configuration"></a>
- Clone this repository
- Open the demo with Android Studio
- Add your Veryfi Artifactory credentials to settings.gradle
- Replace credentials in `CaptureCreditCardActivity.kt` with yours
```
const val CLIENT_ID = "XXX" // replace XXX with your assigned Client Id
const val AUTH_USERNAME = "XXX" // replace XXX with your assigned Username
const val AUTH_APIKEY = "XXX" // replace XXX with your assigned API Keyx
const val URL = "XXX" // replace XXX with your assigned Endpoint URL
```
- Run the project

## Other Lens Android Examples <a name="examples"></a>
You can find some example projects, which are the different versions of Lens that we currently offer:
- [Lens for Long Receipts](https://github.com/veryfi/veryfi-lens-long-receipts-android-demo)
- [Lens for Receipts](https://github.com/veryfi/veryfi-lens-receipts-android-demo)
- [Lens for Credit Cards](https://github.com/veryfi/veryfi-lens-credit-cards-android-demo)
- [Lens for Business Cards](https://github.com/veryfi/veryfi-lens-business-cards-android-demo)
- [Lens for Checks](https://github.com/veryfi/veryfi-lens-checks-android-demo)
- [Lens for W2](https://github.com/veryfi/veryfi-lens-w2-android-demo)
- [Lens for W9](https://github.com/veryfi/veryfi-lens-w9-android-demo)

## Other platforms <a name="other_platforms"></a>
You can find these examples for Lens iOS
- [Long Receipts](https://github.com/veryfi/veryfi-lens-long-receipts-ios-demo)
- [Receipts](https://github.com/veryfi/veryfi-lens-receipts-ios-demo)
- [Credit Cards](https://github.com/veryfi/veryfi-lens-credit-cards-ios-demo)
- [Business Cards](https://github.com/veryfi/veryfi-lens-business-cards-ios-demo)
- [Checks](https://github.com/veryfi/veryfi-lens-checks-ios-demo)

We also support the following wrappers for hybrid frameworks:
- [Cordova](https://hub.veryfi.com/lens/docs/cordova/)
- [React Native](https://hub.veryfi.com/lens/docs/react-native/)
- [Flutter](https://hub.veryfi.com/lens/docs/flutter/)
- [Xamarin](https://hub.veryfi.com/lens/docs/xamarin/)

If you don't have access to our Hub, please contact our sales team, you can find the contact bellow.

## Get in contact with our sales team <a name="contact"></a>
Contact sales@veryfi.com to learn more about Veryfi's awesome products.
