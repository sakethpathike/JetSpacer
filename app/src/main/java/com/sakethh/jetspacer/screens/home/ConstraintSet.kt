package com.sakethh.jetspacer.screens.home

import androidx.constraintlayout.compose.ConstraintSet


val constraintSet = ConstraintSet {

    val cardIconConstraintRef = createRefFor("cardIcon")
    val cardTitleConstraintRef = createRefFor("cardTitle")
    val titleWithIconConstraintRef = createRefFor("titleWithIcon")
    val cardDescriptionConstraintRef = createRefFor("cardDescription")

    val apodMediaConstraintRef = createRefFor("apodMedia")
    val apodTitleConstraintRef = createRefFor("apodTitle")
    val apodDescriptionConstraintRef = createRefFor("apodDescription")
    val apodIconConstraintRef = createRefFor("apodIcon")
    val apodDropDownIconConstraintRef = createRefFor("apodDropDownIcon")

    val changeAPODDate = createRefFor("changeAPODDate")

    val moreOptionsIcon = createRefFor("moreOptionsIcon")
    val moreOptionsDropDown = createRefFor("moreOptionsDropDown")
    val bookMarkIcon = createRefFor("bookMarkIcon")
    val downloadIcon = createRefFor("downloadIcon")


    constrain(cardIconConstraintRef) {
        top.linkTo(parent.top)
        start.linkTo(parent.start)
    }
    constrain(cardTitleConstraintRef) {
        top.linkTo(cardIconConstraintRef.top)
        start.linkTo(cardIconConstraintRef.end)
        bottom.linkTo(cardDescriptionConstraintRef.top)
    }
    constrain(cardDescriptionConstraintRef) {
        top.linkTo(cardTitleConstraintRef.bottom)
        start.linkTo(cardIconConstraintRef.end)
        bottom.linkTo(cardIconConstraintRef.bottom)
    }
    constrain(titleWithIconConstraintRef) {
        top.linkTo(cardIconConstraintRef.top)
        start.linkTo(cardIconConstraintRef.end)
        bottom.linkTo(cardIconConstraintRef.bottom)
    }


    constrain(apodMediaConstraintRef) {
        top.linkTo(parent.top)
        start.linkTo(parent.start)
        end.linkTo(parent.end)
    }
    constrain(apodIconConstraintRef) {
        top.linkTo(apodMediaConstraintRef.bottom)
        start.linkTo(parent.start)
    }
    constrain(apodTitleConstraintRef) {
        top.linkTo(apodIconConstraintRef.top)
        start.linkTo(apodIconConstraintRef.end)
        bottom.linkTo(apodDescriptionConstraintRef.top)
    }
    constrain(apodDescriptionConstraintRef) {
        top.linkTo(apodTitleConstraintRef.bottom)
        start.linkTo(apodIconConstraintRef.end)
        bottom.linkTo(apodIconConstraintRef.bottom)
    }
    constrain(changeAPODDate) {
        top.linkTo(apodDescriptionConstraintRef.bottom)
        start.linkTo(apodDescriptionConstraintRef.start)
    }
    constrain(apodDropDownIconConstraintRef) {
        top.linkTo(apodTitleConstraintRef.top)
        bottom.linkTo(parent.bottom)
        end.linkTo(parent.end)
    }


    constrain(moreOptionsIcon) {
        top.linkTo(apodMediaConstraintRef.top)
        end.linkTo(apodMediaConstraintRef.end)
    }
    constrain(moreOptionsDropDown) {
        top.linkTo(apodMediaConstraintRef.top)
        end.linkTo(apodMediaConstraintRef.end)
    }
    constrain(downloadIcon) {
        bottom.linkTo(bookMarkIcon.top)
        end.linkTo(apodMediaConstraintRef.end)
    }
    constrain(bookMarkIcon) {
        bottom.linkTo(apodMediaConstraintRef.bottom)
        end.linkTo(apodMediaConstraintRef.end)
    }

}