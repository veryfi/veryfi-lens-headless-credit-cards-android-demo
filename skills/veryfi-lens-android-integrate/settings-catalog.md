# VeryfiLensSettings catalog (Android)

Generated from `veryfihelpers/src/main/java/com/veryfi/lens/helpers/VeryfiLensSettings.kt` at SDK `2.7.0.6` (commit `7f33726c`): every `@Setting` property, in source order. "Meaning" is the SDK KDoc (trimmed; blank when the SDK has none). "Notes" are integration guidance from this pack — see [conflicts.md](conflicts.md) and [clarifications.md](clarifications.md).

Usage: `VeryfiLensSettings().apply { propertyName = value }`. The JSON key is the name used by dashboard `lens_settings` / `initWithJsonObject` (relevant only when `ignoreRemoteSettings = false`).

Enums (nested in `VeryfiLensSettings`):

| Enum | Values |
|------|--------|
| `ExtractionEngine` | `VeryfiCloudAPI` ("api", default), `VeryfiInApp` ("mobile"), `None` ("none") |
| `CreditCardAutoCaptureMode` | `Speed`, `Normal` (default), `Accuracy` |
| `CameraProcessingMode` | `Document` (default), `Card` |
| `OcrType` | `Strips` (default), `Caps` |
| `AnyDocumentType` (deprecated) | `INE`, `Passport`, `ColombianId`, `DriverLicense`, `HealthCare` |
| `DocumentFields` | `Vendor`, `Date`, `Time`, `Total`, `Tax` |
| `SameFileUploadPreventionPolicy` | `None` (default), `Session`, `Always` |

`com.veryfi.lens.helpers.DocumentType`: `CREDIT_CARD`, `BUSINESS_CARD`, `RECEIPT`, `LONG_RECEIPT`, `ANY_DOCUMENT`, `INVOICE`, `OTHER`, `CHECK`, `CODE`, `W2`, `W9`, `BARCODE`, `BANK_STATEMENT`, `INSURANCE_CARD`, `PASSPORT`, `DRIVER_LICENSE`, `NUTRITION_FACT`, `SHIPPING_LABEL`, `PRESCRIPTION_LABEL`.

Non-`@Setting` public `var`s on the class (`attachDocumentMode`, `awsRegion`, `categoriesList`, `category`, `coreTags`, `costCode(s)`, `customer(s)`, `floatMenu`, `notes`, `receiptId`, `tagsSelected`, `whatsapp*`, `autoLightEnabled`) are SDK/Core internals — do not set.

| Property | JSON key | Type | Default | Meaning (KDoc) | Notes |
|----------|----------|------|---------|----------------|-------|
| `accentColor` | `accent_color` | String? | `"#005AC1"` | accent color used by material design |  |
| `accentDarkColor` | `accent_dark_color` | String? | `"#DBE2F9"` | accent dark color used by material design |  |
| `afterScanCloseCameraIsOn` | `after_scan_close_camera_is_on` | Boolean | `true` | enables/disables the option in the menu | More-menu row only. |
| `alertSheetStyleIsOn` | `alert_sheet_style_is_on` | Boolean | `true` |  |  |
| `allowCorrectingLocalExtractedFields` | `allow_correcting_local_extracted_fields` | Boolean | `false` | enables/disables drag-and-drop of locally extracted field overlays on the submit preview so users can… |  |
| `allowSubmitBlurredDocsIsOn` | `allow_submit_blurred_docs_is_on` | Boolean | `false` | enables/disables allowing blurred images to be uploaded |  |
| `allowSubmitLowQualityDocsIsOn` | `allow_submit_low_quality_docs_is_on` | Boolean | `true` | enables/disables allowing low quality images to be uploaded |  |
| `allowSubmitUndetectedDocsIsOn` | `allow_submit_undetected_docs_is_on` | Boolean | `true` | enables/disables allowing the upload of blurry or not detected documents | Umbrella: also lets blur and glare dialogs continue. |
| `allowSubmitCheckCornersNotDetected` | `allow_submit_check_corners_not_detected` | Boolean | `true` | enables/disables allowing check submission when corners are not detected |  |
| `allowSubmitCheckSideNotDetected` | `allow_submit_check_side_not_detected` | Boolean | `true` | enables/disables allowing check submission when the expected side is not detected |  |
| `allowSubmittingDocumentWithoutFields` | `allow_submitting_document_without_fields` | Boolean | `true` | enables/disables allowing the submission of documents if any documentFieldsToDetect field is missing |  |
| `allowSubmittingScreenshots` | `allow_submitting_screenshots` | Boolean | `true` | enables/disables allowing the submission of screenshots, default true | Imported screenshot files. |
| `animationDisplayDuration` | `animation_display_duration` | Double | `0.0` |  |  |
| `anyDocumentBackIsOn` | `any_document_back_is_on` | Boolean | `false` | enables prompting to capture the back of an AnyDocs document | Needs `packageMaxScans == 2`. |
| `anyDocumentBackIsRequired` | `any_document_back_is_required` | Boolean | `false` | when anyDocumentBackIsOn, requires capturing the document back |  |
| `anyDocumentTemplate` | `any_document_template` | String | `""` | picks which document template will be used for data extraction |  |
| `anyDocumentType` | `any_document_type` | AnyDocumentType? | `null` | set supported OCR extraction, possible options ine, passport, colombian_id | Deprecated local regex OCR; do not set. |
| `appVersion` | `app_version` | String? | `null` | version of the Lens SDK client app |  |
| `autoCaptureFrameConfidence` | `auto_capture_frame_confidence` | Int | `10` | sets the number of valid frames that account for auto capture done, 10 by default |  |
| `autoCaptureMarginRatio` | `auto_capture_margin_ratio` | Double | `0.0` | Value from 0 up to 0.1 with a sweet spot at 0.08 to force a the document to be at a certain distance from… |  |
| `captureMarginRatio` | `capture_margin_ratio` | Double | `0.0` | Value from 0 up to 0.1 with a sweet spot at 0.08 to force a the document to be at a certain distance from… |  |
| `autoCaptureIsOn` | `auto_capture_is_on` | Boolean | `false` | if on the app will capture the document when is the best frame | Forced true for CREDIT_CARD; false for LONG_RECEIPT on prefs load. Overridden while `moreMenuIsOn`. |
| `autoCaptureOtherIsOn` | `auto_capture_other_is_on` | Boolean | `true` | if on the app will capture the document when is the best frame for other document type | Footgun: set false. |
| `autoCropBrowserIsOn` | `auto_crop_browser_is_on` | Boolean | `false` | enables option to auto-crop document from browser |  |
| `autoCropGalleryIsOn` | `auto_crop_gallery_is_on` | Boolean | `false` | enables option to auto-crop document from gallery | Also used by `uploadImages` and Headless `processGalleryImage`. |
| `autoDeleteAfterProcessing` | `auto_delete_after_processing` | Boolean | `false` | auto delete document from server after processing |  |
| `autoDeleteLocalResourcesAfterProcessing` | `auto_delete_local_resources_after_processing` | Boolean | `true` | auto delete the documents saved locally | false: host owns deletion. |
| `autoDocDetectionAndCropIsOn` | `auto_doc_detection_and_crop_is_on` | Boolean | `true` | forces document detection to ON overwritting memory settings | Overridden while `moreMenuIsOn`. Required for import auto-crop. |
| `autoLightDetectionIsOn` | `auto_light_detection_is_on` | Boolean | `true` | if on the room ambience controls light to illuminate the document. Turn OFF for manual controls | Forced false for CREDIT_CARD. Overridden while `moreMenuIsOn`. |
| `autoRotateIsOn` | `auto_rotate_is_on` | Boolean | `false` | enables option to auto-rotate document |  |
| `autoSelectManualCropIsOn` | `auto_select_manual_crop_is_on` | Boolean | `false` | enables/disables the option to select the starting area by drawing a line with your finger |  |
| `autoSkewCorrectionIsOn` | `auto_skew_correction_is_on` | Boolean | `false` | if on enable auto-skew model correction |  |
| `autoSubmitDocumentOnCapture` | `auto_submit_document_on_capture` | Boolean | `false` | auto submit document on capture without show the preview screen |  |
| `autoTagDeviceId` | `auto_tag_device_id` | Boolean | `false` | enable to add information about the device id |  |
| `autoTagLensVersion` | `auto_tag_lens_version` | Boolean | `false` | enable to add information about the Lens version |  |
| `autoTagPlatform` | `auto_tag_platform` | Boolean | `false` | enable to add information about the platform |  |
| `autoTagSource` | `auto_tag_source` | Boolean | `false` | enable to get in the response if the image was captured from gallery, or the camera, or with browse option |  |
| `backgroundColorConfirmationScreen` | `background_color_confirmation_screen` | String? | `"#000000"` | color used for the background of the confirmation screen |  |
| `backgroundDarkColorConfirmationScreen` | `background_dark_color_confirmation_screen` | String? | `"#000000"` | color used for the background of the confirmation screen |  |
| `backupDocsToGallery` | `backup_docs_to_gallery` | Boolean | `true` | uses photo gallery to backup each scans -- NOTE: must ask user for permission | Set false (base hardening). Overridden by stored prefs while `moreMenuIsOn`. |
| `barcodeExtractionIsOn` | `barcode_extraction_is_on` | Boolean | `false` | enables/disables to read the barcode in the receipt or bill |  |
| `batchUploadOnCamera` | `batch_upload_on_camera` | Boolean | `false` | enables/disables the batch upload feature when using the camera | Overridden while `moreMenuIsOn` (Superman mode). |
| `batchUploadAnimationDuration` | `batch_upload_animation_duration` | Double | `0.8` | duration in seconds for the batch-upload "vee" fly-by on camera; **0** disables the animation |  |
| `blurDetectionInAutocaptureIsOn` | `blur_detection_in_autocapture_is_on` | Boolean | `false` | if enabled, it will run blur detection against the auto-captured image |  |
| `blurDetectionIsOn` | `blur_detection_is_on` | Boolean | `true` | checks if a picture captured has 20% blur - blurred receipts don't process well | Overridden while `moreMenuIsOn`. |
| `blurThreshold` | `blur_threshold` | Double | `0.5` | score (0.0 - 1.0) above which a scan is considered blurred by the on-device blur detector. Values outside… |  |
| `boostModeIsOn` | `boost_mode_is_on` | Boolean | `false` | Flag that tells Veryfi whether boost mode should be enabled |  |
| `boundingBoxesIsOn` | `bounding_boxes_is_on` | Boolean | `false` | returns coordinates for where fields are located on the document |  |
| `brandImage` | `brand_image` | Int? | `null` | image to be used in long stitching receipt feature |  |
| `browseDocumentDetectorIsOn` | `browse_document_detector_is_on` | Boolean | `true` | enable the document detector option from the browse |  |
| `browseIsOn` | `browse_is_on` | Boolean | `true` | enable option to search a image from the gallery | More-menu Import card only. |
| `browseOtherIsOn` | `browse_other_is_on` | Boolean | `false` | enable/disables option to search a image from the gallery |  |
| `browserMultipleSelectionIsOn` | `browser_multiple_selection_is_on` | Boolean | `false` |  |  |
| `buttonFont` | `button_font` | String? | `""` | allows the use of a custom font for the buttons |  |
| `buttonFontSize` | `button_font_size` | Int | `16` | sets the button font size |  |
| `buttonFullWidth` | `button_full_width` | Boolean | `false` | sets the button to full width |  |
| `buttonHeight` | `button_height` | Int | `40` | sets the button height |  |
| `buttonHorizontalPadding` | `button_horizontal_padding` | Int | `16` | sets the button horizontal padding |  |
| `buttonTopPadding` | `button_top_padding` | Int | `16` | sets the button top padding |  |
| `cameraProcessingMode` | `camera_processing_mode` | CameraProcessingMode | `CameraProcessingMode.Document` | Pick mode for scanning in camera between document and card modes. This setting is only valid when using… | Only for ANY_DOCUMENT. |
| `categories` | `categories` | ArrayList<String>? | `null` | optional list of custom categories for system to use in working out which category this doc belongs to |  |
| `checkSequenceMode` | `check_sequence_mode` | Boolean | `false` | enables/disables the check sequence mode |  |
| `checksBackIsOn` | `checks_back_is_on` | Boolean | `false` | enables/disables the capture of checks back side after front side is taken |  |
| `checksCropMargin` | `checks_crop_margin` | Int | `5` | decides how much margin should be left around checks edges |  |
| `checksBackIsRequired` | `checks_back_is_required` | Boolean | `false` | force the capture of checks back side after front side is taken |  |
| `checksMinAspectRatio` | `checks_min_aspect_ratio` | Double | `0.0` |  |  |
| `checksMaxAspectRatio` | `checks_max_aspect_ratio` | Double | `0.0` |  |  |
| `cleanBorderIsOn` | `clean_border_is_on` | Boolean | `false` | enables/disables the clean-borders post processing |  |
| `clearSessionOnBack` | `clear_session_on_back` | Boolean | `true` | clear the session on back button |  |
| `closeCameraOnSubmit` | `close_camera_on_submit` | Boolean | `true` | after confirming the picture, will close the whole Capture process | Overridden while `moreMenuIsOn`; ignored when More + More>Settings are on. |
| `collectNotesIsOn` | `collect_notes_is_on` | Boolean | `false` | shows ExtraData / Add Notes on the submit preview so notes can be attached to the document. Does not… |  |
| `computeIsOn` | `compute_is_on` | Boolean | `true` | Flag that tells Veryfi whether compute feature should be enabled |  |
| `confidenceDetailsIsOn` | `confidence_details_is_on` | Boolean | `false` | enable some scores about the confidence level of the inference |  |
| `continuousAutoFocus` | `continuous_auto_focus` | Boolean | `false` | enable continuous auto focus |  |
| `cropCornersColor` | `crop_corners_color` | String? | `null` | color used for the crop region's corners and grid |  |
| `cropCornersDarkColor` | `crop_corners_dark_color` | String? | `null` | color used for the crop region's corners and grid in dark mode |  |
| `cropLayoutIsOn` | `crop_layout_is_on` | Boolean | `false` |  | Fixed frame with `autoDocDetectionAndCropIsOn = false`. |
| `cropLayoutWithAutoDetection` | `crop_layout_with_auto_detection` | Boolean | `false` | enables document detection and the green box inside the crop layout | Hybrid needs `cropLayoutIsOn` too. |
| `cropLayoutBorderColor` | `crop_layout_border_color` | String | `"#00000000"` |  |  |
| `cropLayoutStroke` | `crop_layout_stroke` | Int | `0` |  |  |
| `cropLayoutCornerRadius` | `crop_layout_corner_radius` | Int | `0` |  |  |
| `cropLayoutWidth` | `crop_layout_width` | Int | `80` |  |  |
| `cropLayoutHeight` | `crop_layout_height` | Int | `35` |  |  |
| `cropLayoutAspectRatio` | `crop_layout_aspect_ratio` | Double | `0.0` |  |  |
| `cropLayoutTipMessage` | `crop_layout_tip_message` | String? | `null` |  |  |
| `cropLayoutOverlayAlpha` | `crop_layout_overlay_alpha` | Double | `0.3` |  |  |
| `cropLayoutOverlayColor` | `crop_layout_overlay_color` | String? | `"#A6000000"` |  |  |
| `cropLayoutPrimaryTextColor` | `crop_layout_primary_text_color` | String? | `null` |  |  |
| `cropLayoutSecondaryTextColor` | `crop_layout_secondary_text_color` | String? | `null` |  |  |
| `cropLayoutPrimaryTextBackgroundColor` | `crop_layout_primary_text_background_color` | String? | `null` |  |  |
| `cropLayoutSecondaryTextBackgroundColor` | `crop_layout_secondary_text_background_color` | String? | `null` |  |  |
| `forceCropLayoutOnRetake` | `force_crop_layout_on_retake` | Boolean | `false` |  |  |
| `cropLayoutShowDelay` | `crop_layout_show_delay` | Double | `0.0` |  |  |
| `cropTipAlertCounter` | `crop_tip_alert_counter` | Int | `2` | number used to set the number of times to display the crop tip alert |  |
| `creditCardAutoCaptureMode` | `credit_card_auto_capture_mode` | CreditCardAutoCaptureMode | `CreditCardAutoCaptureMode.Normal` |  | `Speed` / `Normal` / `Accuracy`. |
| `creditCardDetectCardCVC` | `credit_card_detect_card_cvc` | Boolean | `true` |  |  |
| `creditCardDetectCardDate` | `credit_card_detect_card_date` | Boolean | `true` |  |  |
| `creditCardDetectCardHolderName` | `credit_card_detect_card_holder_name` | Boolean | `true` |  |  |
| `creditCardDetectCardNumber` | `credit_card_detect_card_number` | Boolean | `true` |  | Headless CC + UI CC. |
| `creditCardMarginBottom` | `credit_card_margin_bottom` | Int | `0` |  |  |
| `creditCardMarginTop` | `credit_card_margin_top` | Int | `100` |  |  |
| `customHeaders` | `custom_headers` | Map<String, String>? | `null` | Dictionary ofcustom HTTP headers |  |
| `dataExtractionEngine` | `data_extraction_engine` | ExtractionEngine? | `ExtractionEngine.VeryfiCloudAPI` | Which engine to use for data extraction (Veryfi Cloud, Veryfi In-App) | Forced `VeryfiInApp` for CREDIT_CARD. |
| `defaultSelectedDocumentType` | `default_selected_document_type` | DocumentType? | `null` | which is the default selected document type |  |
| `detectBlurResponseIsOn` | `detect_blur_response_is_on` | Boolean | `false` | enables is_blurry which represents the model’s assessment about whether the document is blurred or not |  |
| `dewarpingIsOn` | `dewarping_is_on` | Boolean | `false` | enables/disables dewarping algorithm |  |
| `deviceUserUuid` | `device_user_uuid` | String? | `""` | allow passing uuid4 of our customer's unique user login |  |
| `dictateIsOn` | `dictate_is_on` | Boolean | `true` | enables/disables the round button in the header to capture expenses via voice (dictation) | Obsolete; set false. |
| `dialogBackgroundColor` | `dialog_background_color` | String? | `null` | color used for the dialog background |  |
| `dialogBackgroundColorDark` | `dialog_background_color_dark` | String? | `null` | color used for the dialog background for dark mode |  |
| `dialogLeftButtonBackgroundColor` | `dialog_left_button_background_color` | String? | `null` | color used for the background in the left button in the dialogs |  |
| `dialogLeftButtonBackgroundColorDark` | `dialog_left_button_background_color_dark` | String? | `null` | color used for the background in the left button in the dialogs for dark mode |  |
| `dialogLeftButtonBorderColor` | `dialog_left_button_border_color` | String? | `null` | color used for the border in the left button in the dialogs |  |
| `dialogLeftButtonBorderColorDark` | `dialog_left_button_border_color_dark` | String? | `null` | color used for the border in the left button in the dialogs for dark mode |  |
| `dialogLeftButtonBorderStroke` | `dialog_left_button_border_stroke` | Int | `0` | width used for the border in the left button in the dialogs |  |
| `dialogLeftButtonTextColor` | `dialog_left_button_text_color` | String? | `null` | color used for the text in the left button in the dialogs |  |
| `dialogLeftButtonTextColorDark` | `dialog_left_button_text_color_dark` | String? | `null` | color used for the text in the left button in the dialogs for dark mode |  |
| `dialogMessageColor` | `dialog_message_color` | String? | `null` | color used for the message in the dialogs |  |
| `dialogMessageColorDark` | `dialog_message_color_dark` | String? | `null` | color used for the message in the dialogs in dark mode |  |
| `dialogRightButtonBackgroundColor` | `dialog_right_button_background_color` | String? | `null` | color used for the background in the right button in the dialogs |  |
| `dialogRightButtonBackgroundColorDark` | `dialog_right_button_background_color_dark` | String? | `null` | color used for the background in the right button in the dialogs for dark mode |  |
| `dialogRightButtonBorderColor` | `dialog_right_button_border_color` | String? | `null` | color used for the border in the right button in the dialogs |  |
| `dialogRightButtonBorderColorDark` | `dialog_right_button_border_color_dark` | String? | `null` | color used for the border in the right button in the dialogs for dark mode |  |
| `dialogRightButtonBorderStroke` | `dialog_right_button_border_stroke` | Int | `0` | width used for the border in the right button in the dialogs |  |
| `dialogRightButtonTextColor` | `dialog_right_button_text_color` | String? | `null` | color used for the text in the right button in the dialogs |  |
| `dialogRightButtonTextColorDark` | `dialog_right_button_text_color_dark` | String? | `null` | color used for the text in the right button in the dialogs for dark mode |  |
| `dialogButtonsVerticalLayoutIsOn` | `dialog_buttons_vertical_layout_is_on` | Boolean | `false` | when true, stacks dialog action buttons vertically so long labels are not clipped; default false (horizontal) |  |
| `alertDialogBackgroundColor` | `alert_dialog_background_color` | String? | `null` | color used for the alert dialog background |  |
| `alertDialogBackgroundColorDark` | `alert_dialog_background_color_dark` | String? | `null` | color used for the alert dialog background for dark mode |  |
| `alertDialogMessageColor` | `alert_dialog_message_color` | String? | `null` | color used for the message in the alert dialogs |  |
| `alertDialogMessageColorDark` | `alert_dialog_message_color_dark` | String? | `null` | color used for the message in the alert dialogs in dark mode |  |
| `alertDialogLeftButtonTextColor` | `alert_dialog_left_button_text_color` | String? | `null` | color used for the text in the left button in the alert dialogs |  |
| `alertDialogLeftButtonTextColorDark` | `alert_dialog_left_button_text_color_dark` | String? | `null` | color used for the text in the left button in the alert dialogs for dark mode |  |
| `alertDialogLeftButtonBackgroundColor` | `alert_dialog_left_button_background_color` | String? | `null` | color used for the background in the left button in the alert dialogs |  |
| `alertDialogLeftButtonBackgroundColorDark` | `alert_dialog_left_button_background_color_dark` | String? | `null` | color used for the background in the left button in the alert dialogs for dark mode |  |
| `alertDialogRightButtonTextColor` | `alert_dialog_right_button_text_color` | String? | `null` | color used for the text in the right button in the alert dialogs |  |
| `alertDialogRightButtonTextColorDark` | `alert_dialog_right_button_text_color_dark` | String? | `null` | color used for the text in the right button in the alert dialogs for dark mode |  |
| `alertDialogRightButtonBackgroundColor` | `alert_dialog_right_button_background_color` | String? | `null` | color used for the background in the right button in the alert dialogs |  |
| `alertDialogRightButtonBackgroundColorDark` | `alert_dialog_right_button_background_color_dark` | String? | `null` | color used for the background in the right button in the alert dialogs for dark mode |  |
| `docDetectFillUIColor` | `doc_detect_fill_ui_color` | String? | `"#9653BF8A"` | Document Detection Rectangle Fill Color: nil default is VERYFI_GREEN |  |
| `docDetectFillUIColorDark` | `doc_detect_fill_ui_color_dark` | String? | `"#9653BF8A"` | Document Detection Rectangle Fill Color: nil default is VERYFI_GREEN |  |
| `docDetectStrokeUIColor` | `doc_detect_stroke_ui_color` | String? | `null` | Document Detection Rectangle Stroke Color: nil default is clearColor |  |
| `docDetectStrokeUIColorDark` | `doc_detect_stroke_ui_color_dark` | String? | `null` | Document Detection Rectangle Stroke Color: nil default is clearColor |  |
| `documentFieldsToDetect` | `document_fields_to_detect` | ArrayList<DocumentFields>? | `null` |  | Paid; Field Detection / Remote Models / Lite artifact. |
| `documentFieldsUICornerRadius` | `document_fields_ui_corner_radius` | Int | `30` |  |  |
| `documentFieldsBackgroundColor` | `document_fields_background_color` | String? | `"#04581a"` |  |  |
| `documentFieldsBackgroundColorDark` | `document_fields_background_color_dark` | String? | `"#04581a"` |  |  |
| `documentMissingFieldsIconColor` | `document_missing_fields_icon_color` | String? | `"#ff0000"` |  |  |
| `documentMissingFieldsIconColorDark` | `document_missing_fields_icon_color_dark` | String? | `"#ff0000"` |  |  |
| `documentFoundFieldsIconColor` | `document_found_fields_icon_color` | String? | `"#04581a"` |  |  |
| `documentFoundFieldsIconColorDark` | `document_found_fields_icon_color_dark` | String? | `"#04581a"` |  |  |
| `documentFieldsTextColor` | `document_fields_text_color` | String? | `"#ffffff"` |  |  |
| `documentFieldsTextColorDark` | `document_fields_text_color_dark` | String? | `"#ffffff"` |  |  |
| `documentTypes` | `document_types` | ArrayList<DocumentType>? | `null` | list of supported document types. Supported values: "long_receipt", "receipt", "invoice", "other".… | Required; must match artifact flavor. |
| `documentTypesTextColor` | `document_types_text_color` | String? | `null` | color used for the text item in the Document Types list |  |
| `documentTypesTextColorDark` | `document_types_text_color_dark` | String? | `null` | color used for the text item in the Document Types list for dark mode |  |
| `driverLicenseBackIsOn` | `driver_license_back_is_on` | Boolean | `true` | enables/disables prompting to capture the driver's license back after the front is captured | Android default true. |
| `driverLicenseBackIsRequired` | `driver_license_back_is_required` | Boolean | `false` | when driverLicenseBackIsOn, requires capturing the driver's license back |  |
| `insuranceCardBackIsOn` | `insurance_card_back_is_on` | Boolean | `true` | enables/disables prompting to capture the insurance card back after the front is captured | Android default true. |
| `insuranceCardBackIsRequired` | `insurance_card_back_is_required` | Boolean | `false` | when insuranceCardBackIsOn, requires capturing the insurance card back |  |
| `emailCCDomain` | `email_cc_domain` | String | `"veryfi.cc"` | String of the domain name used to power emailed documents | Core email only. |
| `emailCCIsOn` | `email_cc_is_on` | Boolean | `true` | enables/disables the email cc view inside settings | Core email only. |
| `enableScreenshots` | `enable_screenshots` | Boolean | `true` | enables/disables taking screenshots to the camera screen | Device screenshots of Lens screens, not imported files. |
| `externalId` | `external_id` | String? | `""` | a pass-through field to add a unique reference identifier for a scan which can be used to map back to your… |  |
| `identityVerificationIsOn` | `identity_verification_is_on` | Boolean | `false` | enables/disables the identity verification option | Demo uses `VeryfiInApp`. |
| `identityVerificationThreshold` | `identity_verification_threshold` | Float | `40f` | sets the threshold angle for identity verification |  |
| `font` | `font` | String? | `""` | allows the use of a custom font |  |
| `forceLandscapeImage` | `force_landscape_image` | Boolean | `false` | enables/disables the auto-rotate feature for Checks |  |
| `fraudDetectionIsOn` | `fraud_detection_is_on` | Boolean | `false` | enables/disables the Fraud detection | Also needs backend `detect_fraud_on`. |
| `deviceBlockListIsOn` | `device_block_list_is_on` | Boolean | `false` | when true, checks whether the device is blocked from using Lens via GET… | Needs network + credentials. |
| `galleryButtonWithFilesIsOn` | `gallery_button_with_files_is_on` | Boolean | `false` | when true, tapping the camera gallery control presents a choice between the photo library and the file… |  |
| `galleryDocumentDetectorIsOn` | `gallery_document_detector_is_on` | Boolean | `true` | enable the document detector option from the gallery |  |
| `galleryIcon` | `gallery_icon` | Boolean | `false` | enables/disables the gallery icon |  |
| `galleryIsOn` | `gallery_is_on` | Boolean | `true` | enables/disables the option to use the gallery | Forced false for CREDIT_CARD / CODE on prefs load. |
| `galleryMultipleSelectionIsOn` | `gallery_multiple_selection_is_on` | Boolean | `false` | enables/disables the option to select multiple images from the gallery |  |
| `galleryOtherIsOn` | `gallery_other_is_on` | Boolean | `true` | enables/disables the option to use the gallery for other document type |  |
| `glareDetectionIsOn` | `glare_detection_is_on` | Boolean | `true` | checks if a picture captured has glare - glare receipts don't process well | Android default true; suboptimal; usually false. |
| `hapticFeedbackIsOn` | `haptic_feedback_is_on` | Boolean | `true` | enables/disables haptic feedback for capture, detection, and long-receipt preview events |  |
| `highQualityThumbnail` | `high_quality_thumbnail` | Boolean | `false` | scales x2 local generated thumbnail, default false |  |
| `gpuIsOn` | `gpu_is_on` | Boolean | `true` | enables/disables the gpu to boost the crop models | Device-dependent; leave default unless Veryfi advises. |
| `ignoreRemoteSettings` | `ignore_remote_settings` | Boolean | `false` | Ignores remote control of Lens settings, default false | Set true. |
| `imagePreviewIsOn` | `image_preview_is_on` | Boolean | `true` | enables/disables the ability to show an image preview before to submit |  |
| `isReactNative` | `is_react_native` | Boolean | `false` | indicates if is a React Native app | Wrapper-internal; do not set. |
| `isReimbursableDefault` | `is_reimbursable_default` | Boolean | `false` | enables/disables reimbursable option for documents |  |
| `locationServicesIsOn` | `location_services_is_on` | Boolean | `false` | enables/disables location services to grab user's lat & lng | Triggers runtime location request. |
| `manualCropIsOn` | `manual_crop_is_on` | Boolean | `true` | enables/disables the option to use manual crop with the fingers |  |
| `moreMenuIsOn` | `more_menu_is_on` | Boolean | `true` | enables/disables the showing of the more menu | Stored user prefs override host values on configure. Set false. |
| `moreSettingsMenuIsOn` | `more_settings_menu_is_on` | Boolean | `true` | enables/disables the showing of the more > settings option | Set false. |
| `multipleDocumentsIsOn` | `multiple_documents_is_on` | Boolean | `true` | enables/disables processing multiple stitched images as different transactions, this requires stitchIsOn |  |
| `multiplePagesCaptureIsOn` | `multiple_pages_capture_is_on` | Boolean | `true` | enables/disables the ability to capture multiple documents in one transaction |  |
| `noCompress` | `no_compress` | Boolean | `false` | enables/disables image compresion |  |
| `noCompressOnWifi` | `no_compress_on_wifi` | Boolean | `false` | enables/disables compresion when is connected to Wifi |  |
| `nonInteractiveBlurCheckIsOn` | `non_interactive_blur_check_is_on` | Boolean | `false` | enables/disables non-interactive blur check | `uploadImages`: blurred → `blurred_image`. |
| `notificationChannelName` | `notification_channel_name` | String | `"notificationChannelName"` | String sent as notification channel name |  |
| `notificationSmallIcon` | `notification_small_icon` | Int | `0` | drawable resource id from the host base module used as the | Lite: base-module drawable or 0. |
| `ocrBorderColor` | `ocr_border_color` | String? | `"#48E195"` | Border color for the OCR view to detect codes |  |
| `ocrBorderStroke` | `ocr_border_stroke` | Int | `10` | Width for the OCR circular view border, only when ocrType is set to 'caps' | Caps only. |
| `ocrRadius` | `ocr_radius` | Int? | `60` | Radius for the OCR circle view (only when ocrType is set to 'caps') | Caps only. |
| `ocrRegex` | `ocr_regex` | String? | `null` | Regex to improve auto-capture accuracy for mobile OCR | CODE only. |
| `ocrType` | `ocr_type` | OcrType | `OcrType.Strips` | Set the OCR view type to be used (strips or caps) | CODE only: `Strips` / `Caps`. |
| `ocrViewCornerRadius` | `ocr_view_corner_radius` | Int? | `20` | Corner radius in dp for the OCR view to detect codes |  |
| `ocrViewHeight` | `ocr_view_height` | Int? | `8` | Height in dp for the OCR view to detect codes |  |
| `ocrViewWidth` | `ocr_view_width` | Int? | `85` | Width in dp of the OCR view to detect codes |  |
| `originalImageMaxSizeInMB` | `original_image_max_size_in_mb` | Float? | `2.5f` | Setting in MB that controls the max image size to produce |  |
| `originalLongImageMaxSizeInMB` | `original_long_image_max_size_in_mb` | Float | `0f` | controls the max image size for long receipts. When 0.0, originalImageMaxSizeInMB is used instead |  |
| `overrideBaseUrl` | `override_base_url` | String? | `null` | Overrides the base url of the API | Only if Veryfi instructs. |
| `packageMaxScans` | `package_max_scans` | Int? | `5` | allows to increase number of packages in a stitch by defaults is 4 | Android default 5 (KDoc says 4). |
| `parseAddressIsOn` | `parse_address_is_on` | Boolean | `false` | enables vendor.parsed_address which contains a breakdown of the vendor address into its components, such… |  |
| `pdfFormatIsOn` | `pdf_format_is_on` | Boolean | `true` | generates the pdf with a white background |  |
| `pdfEditIsOn` | `pdf_edit_is_on` | Boolean | `false` | enable/disable options to edit the preview bitmap |  |
| `pdfMaxSizeInMB` | `pdf_max_size_in_mb` | Int | `10` | max size for pdf to upload |  |
| `pdfPreviewIsOn` | `pdf_preview_is_on` | Boolean | `false` | enables/disables the ability to show a PDF preview before to submit |  |
| `primaryColor` | `primary_color` | String? | `"#005AC1"` | main color used by material design |  |
| `primaryDarkColor` | `primary_dark_color` | String? | `"#ADC6FF"` | main dark color used by material design |  |
| `retakeScanIsOn` | `retake_scan_is_on` | Boolean | `false` | enables/disables the option to retake the current image |  |
| `retakeScanButtonBackgroundColor` | `retake_scan_button_background_color` | String? | `null` | color used for the retake scan button background |  |
| `retakeScanButtonBackgroundDarkColor` | `retake_scan_button_background_dark_color` | String? | `null` |  |  |
| `retakeScanButtonContentColor` | `retake_scan_button_content_color` | String? | `null` | color used for the retake scan button text |  |
| `retakeScanButtonContentDarkColor` | `retake_scan_button_content_dark_color` | String? | `null` |  |  |
| `returnStitchedPDF` | `return_stitched_pdf` | Boolean | `false` | enables to get the stitched pdf document |  |
| `rotateDocIsOn` | `rotate_doc_is_on` | Boolean | `true` | enables option to rotate (on each press) a document by 90 degree clockwise |  |
| `sameFileUploadPreventionPolicy` | `same_file_upload_prevention_policy` | SameFileUploadPreventionPolicy | `SameFileUploadPreventionPolicy.None` | controls duplicate gallery/file-browser uploads: none (default, existing behavior), session (block… | `Always` persists hashes only with `VeryfiCloudAPI`. |
| `saveLogsIsOn` | `save_logs_is_on` | Boolean | `true` | enables/disables local logs recommended true option | Recommended true. |
| `scanSessionCounterIsOn` | `scan_session_counter_is_on` | Boolean | `false` | enables a session-wide successful-submission counter badge on camera | Badge stays hidden until this is true. |
| `autoScanSessionCounterIsOn` | `auto_scan_session_counter_is_on` | Boolean | `true` | when scanSessionCounterIsOn is also on, Lens updates the badge from successful submissions; when it is off, the host sets the count with VeryfiLens.updateScanSessionCounter | Default true. Host mode is `scanSessionCounterIsOn = true` and this flag false, then `VeryfiLens.updateScanSessionCounter(count)`. |
| `scanSessionCounterBackgroundColor` | `scan_session_counter_background_color` | String | `"#66000000"` | background color for the scan session counter badge (light mode) |  |
| `scanSessionCounterBackgroundDarkColor` | `scan_session_counter_background_dark_color` | String | `"#66000000"` | background color for the scan session counter badge (dark mode) |  |
| `scanSessionCounterTextColor` | `scan_session_counter_text_color` | String | `"#FFFFFF"` | text color for the scan session counter badge (light mode) |  |
| `scanSessionCounterTextDarkColor` | `scan_session_counter_text_dark_color` | String | `"#FFFFFF"` | text color for the scan session counter badge (dark mode) |  |
| `secondaryColor` | `secondary_color` | String? | `"#DBE2F9"` | secondary color used by material design |  |
| `secondaryDarkColor` | `secondary_dark_color` | String? | `"#3F4759"` | secondary dark color used by material design |  |
| `shareLogsIsOn` | `share_logs_is_on` | Boolean | `false` | enables/disables the button to share logs in the preview screen | Unavailable on Lite. |
| `showBrowserButton` | `show_browser_button` | Boolean | `false` | shows file browser button on camera layout |  |
| `showCropConfirmationAlert` | `show_crop_confirmation_alert` | Boolean | `true` | shows a confirmation alert before cropping |  |
| `showDefaultScanningGuide` | `show_default_scanning_guide` | Boolean | `false` | when true, tapping the help button forces Lens' built-in guide animation instead of calling the host app's… |  |
| `showDocumentTypes` | `show_document_types` | Boolean | `false` | shows and tracks the type of document types supported by Veryfi Engine | Needed when more than one type. |
| `showGuideCounter` | `show_guide_counter` | Int | `0` | number used to set the number of times to display the long receipt animation |  |
| `showGuideShortCounter` | `show_guide_short_counter` | Int | `0` | number used to set the number of times to display the short receipt animation |  |
| `showPrescriptionLabelGuideCounter` | `show_prescription_label_guide_counter` | Int | `0` | number used to set the number of times to display the prescription label animation |  |
| `showHelpButton` | `show_help_button` | Boolean | `false` | shows a help button that invokes VeryfiLensDelegate.onHelpButtonClicked() |  |
| `showInfoButton` | `show_info_button` | Fragment? | `null` | show a custom info view | Fragment; also shows browse icon. |
| `cameraInfoView` | `camera_info_view` | Fragment? | `null` | optional view displayed as a banner at the top of the camera screen when set | Fragment banner; `updateCameraInfoView(fragment)`. |
| `singleScanPackages` | `single_scan_packages` | Boolean | `false` | if enabled and multiple pages + stitch is on, then it will treat each scan as a separate document |  |
| `showLCDIsNotAllowed` | `show_lcd_is_not_allowed` | Boolean | `false` | Shows a blocking alert if the fraud detector detects that a scan is taken from LCD screen |  |
| `showNoDocumentDetectedWarning` | `show_no_document_detected_warning` | Boolean | `true` | allows to show or not if the document was not detected |  |
| `showStitchCounterNumber` | `show_stitch_counter_number` | Boolean | `false` | shows the document counter for stitch documents |  |
| `showStitchPreview` | `show_stitch_preview` | Boolean | `true` | shows/hide the preview button of the stitched documents |  |
| `showSupermanModeIsOn` | `show_superman_mode_is_on` | Boolean | `true` | when true, adds the Superman mode toggle to the more menu page. Default true | More-menu row only. |
| `softBinarizationIsOn` | `soft_binarization_is_on` | Boolean | `false` | enables soft binarization filter |  |
| `stitchIsOn` | `stitch_is_on` | Boolean | `true` | enables/disables the option to combine multiple receipts together into a pdf |  |
| `stitchedPDFPixelDensityMultiplier` | `stitched_pdf_pixel_density_multiplier` | Float? | `2f` | multiplier for the image resolution being drawn on the PDF range 1-5 |  |
| `stitchOtherIsOn` | `stitch_other_is_on` | Boolean | `false` | enables/disables the stitch button in the Other document type |  |
| `stitchTitleIsOn` | `stitch_title_is_on` | Boolean | `false` | enables/disables the option to show the stitch button as text format |  |
| `submitButtonBackgroundColor` | `submit_button_background_color` | String? | `"#005AC1"` | color used for submit button background |  |
| `submitButtonBackgroundColorDark` | `submit_button_background_color_dark` | String? | `"#005AC1"` | color used for submit button background |  |
| `submitButtonBorderColor` | `submit_button_border_color` | String? | `"#005AC1"` | color used for submit button border |  |
| `submitButtonBorderColorDark` | `submit_button_border_color_dark` | String? | `"#005AC1"` | color used for submit button border |  |
| `submitButtonBorderStroke` | `submit_button_border_stroke` | Int | `0` | width used for submit button border |  |
| `submitButtonCornerRadius` | `submit_button_corner_radius` | Int | `20` | number used to set submit button corner radius |  |
| `submitButtonFontColor` | `submit_button_font_color` | String? | `"#FFFFFF"` | color used for submit button text |  |
| `submitButtonFontColorDark` | `submit_button_font_color_dark` | String? | `"#FFFFFF"` | color used for submit button text |  |
| `submitWithoutClose` | `submit_without_close` | Boolean | `false` | Allows to submit the document without close the Lens |  |
| `switchCameraIsOn` | `switch_camera_is_on` | Boolean | `false` |  |  |
| `tags` | `tags` | ArrayList<String>? | `null` | optional list of custom tags for the document |  |
| `textFont` | `text_font` | String? | `""` | allows the use of a custom font for the text |  |
| `titleFont` | `title_font` | String? | `""` | allows the use of a custom font for the title |  |
| `toolbarIconsColor` | `toolbar_icons_color` | String? | `"#4A454A"` | color used for the icons in the toolbar |  |
| `toolbarIconsDarkColor` | `toolbar_icons_color_dark` | String? | `"#CEC6D6"` | color used for the icons in the toolbar |  |
| `torchAlwaysVisibleIsOn` | `torch_always_visible_is_on` | Boolean | `false` | show the button even in auto mode. If the user manually toggles the torch, disable auto mode for that session |  |
| `tryAgainLightIsOn` | `try_again_light_is_on` | Boolean | `false` |  |  |
| `uploadWithoutProcessingIsOn` | `upload_without_processing_is_on` | Boolean | `false` | when dataExtractionEngine=None, uploads the package to S3 and returns the data required to perform the API… | Only with `ExtractionEngine.None`. |
| `usesAlternativeOCRModel` | `uses_alternative_ocr_model` | Boolean | `false` | enables/disables different models for OCR extraction |  |
| `validateCheckSides` | `validate_check_sides` | Boolean | `false` |  |  |
| `retrieveCheckTiffIsOn` | `retrieve_check_tiff_is_on` | Boolean | `false` | enables local check TIFF generation and returns the path on success | Remote Models artifact (TIFF model not bundled in Cheques). |
| `rejectInvalidCheckTIFF` | `reject_invalid_check_tiff` | Boolean | `false` | when true (and retrieveCheckTiffIsOn), shows an alert and prompts retake if TIFF could not be generated |  |
| `validateCheckCorners` | `validate_check_corners` | Boolean | `false` |  | Algorithmic, can be aggressive. |
| `validateCheckSize` | `validate_check_size` | Boolean | `false` |  |  |
| `videoDuration` | `video_duration` | Double | `10.0` |  |  |
| `webhookIsOn` | `webhook_is_on` | Boolean | `false` | Flag that tells Veryfi whether async processing is on or not | Async results; handle webhook. |
| `zoomIsOn` | `zoom_is_on` | Boolean | `true` | enables/disables the option to zoom in/out in a range 1-2 |  |
| `useLegacyModels` | `use_legacy_models` | Boolean | `true` |  | No public contract; leave default. |
