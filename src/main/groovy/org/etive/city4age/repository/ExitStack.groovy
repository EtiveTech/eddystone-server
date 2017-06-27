package org.etive.city4age.repository

class ExitStack {

    private def mStack

    ExitStack() {
        mStack = []
    }

    def isEmpty() {
        return !mStack
    }

    def pairPush(pair) {
        if (mStack != null) mStack.push(pair)
    }

    def pairPop() {
        if (isEmpty()) return null
        return mStack.pop()
    }

    def pairPeek() {
        if (isEmpty()) return null
        return mStack.last()
    }

    def locationMatch(another) {
        def i
        for (i = 0; i < mStack.size(); i++){
            if(mStack[i].sameLocation(another)) break
        }
        if (i < mStack.size()) mStack = mStack[0..i]
    }

    def locationCheck(pair) {
        return (!isEmpty() && pairPeek().sameLocation(pair))
    }
}
