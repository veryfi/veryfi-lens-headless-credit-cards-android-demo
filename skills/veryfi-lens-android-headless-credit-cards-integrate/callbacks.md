# Veryfi Lens Android — Headless Credit Cards — callbacks

Source of truth: Veryfi Lens Android SDK **2.7.0.6**, commit `7f33726c`. Names below were read from that tree. Items not fully confirmed are marked **Unconfirmed**. Do not copy repository passwords, keystores, or Demo-only types into the host app.


Delegate: `com.veryfi.lens.headless.creditcards.VeryfiLensHeadlessDelegate`.

`veryfiLensUpdate` (partial) and `veryfiLensSuccess` (done) carry the detector JSON. Keys in `CreditCardExtractionDetector`: `card_number`, `card_name`, `card_type`, `card_dates` (`JSONArray`), `card_cvc`, `card_uuid`.

`veryfiLensError` may include `error`. A missing crop-model key logs `No valid credentials`. `veryfiLensClose` is invoked from the processor close path.

Doc-ready (`notifyServerOfCreditCardReady`) sends booleans from `isNotEmpty()` for `card_number`, `card_name`, `card_cvc`, `card_type`, and attempts `getString` on `card_dates` (a `JSONArray` in the detector JSON — whether that boolean is posted is **Unconfirmed**). `sdk` is `lens-headless-credit-card`. `data_extraction_engine` is `mobile`. The PAN string is not copied into that request; the boolean is `card_number` non-empty.

There is no UI `package_id` upload callback. `uploadImages` is not part of this type.

