/*
 * Copyright: this code is distributed under WTFPL version2
 * In short: You just DO WHAT THE FUCK YOU WANT TO.
 */

package it.haslearnt.aspects;

class PlainOldJavaObject {
    private String string;

    public void wouldYouDareToPassMeANull(String supposedToBeNull) {
    }

    public void setString(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }
}
