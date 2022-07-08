![Veryfi Logo](https://cdn.veryfi.com/logos/veryfi-logo-wide-github.png)

[![License: MIT](https://img.shields.io/badge/License-MIT-green.svg)](https://opensource.org/licenses/MIT)

# Veryfi Lens
Veryfi Lens is code (a framework) with UI for your mobile app to give it document capture superpowers in minutes.

Let Veryfi handle the complexities of frame processing, asset preprocessing, edge routing, and machine vision challenges in document capture. We have been at this for a long time and understand the intricate nature of mobile capture. That’s why we built Lens. Veryfi Lens is built by developers for developers; making the whole process of integrating Lens into your app fast and easy with as few lines as possible.

Veryfi Lens is a Framework: a self-contained, reusable chunks of code and resources you can import into you app.

Lens is built in native code and optimized for fast performance, clean user experience and low memory usage.

You can read further about Lens in Veryfi's dedicated page: https://www.veryfi.com/lens/

## Table of content
1. [Veryfi Lens Headless Android Example](#example)
2. [Configuration](#configuration)
3. [Other platforms](#other_platforms)
4. [Get in contact with our team](#contact)

## Veryfi Lens Headless Credit Cards Android Example <a name="example"></a>
![headless_credit_card](https://user-images.githubusercontent.com/30125790/178044217-05d35b47-eef1-4cd5-8306-3c01ca66d225.gif)

## Other Lens Android Examples <a name="examples"></a>
This is an example of how to use Veryfi Lens Headless Credit Cards in your app, you can find the developer documentation [here](LensHeadlessCreditCards.pdf).
You can find five example projects, which are the five versions of Lens that we currently offer:
- [Lens for Long Receipts](https://github.com/veryfi/veryfi-lens-long-receipts-android-demo)
- [Lens for Receipts](https://github.com/veryfi/veryfi-lens-receipts-android-demo)
- [Lens for Credit Cards](https://github.com/veryfi/veryfi-lens-credit-cards-android-demo)
- [Lens for Business Cards](https://github.com/veryfi/veryfi-lens-business-cards-android-demo)
- [Lens for Checks](https://github.com/veryfi/veryfi-lens-checks-android-demo)

### Configuration <a name="configuration"></a>
- Clone this repository
- Open the demo with Android Studio
- Add your Veryfi Artifactory credentials to settings.gradle
- Replace credentials in `Application.kt` with yours
```
const val CLIENT_ID = "XXX" // replace XXX with your assigned Client Id
const val AUTH_USERNAME = "XXX" // replace XXX with your assigned Username
const val AUTH_APIKEY = "XXX" // replace XXX with your assigned API Keyx
const val URL = "XXX" // replace XXX with your assigned Endpoint URL
```
- Run the project

### Other platforms <a name="other_platforms"></a>
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

### Get in contact with our sales team <a name="contact"></a>
Contact sales@veryfi.com to learn more about Veryfi's awesome products.
