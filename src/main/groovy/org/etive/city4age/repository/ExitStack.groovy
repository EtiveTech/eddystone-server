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

    def locationExists(another) {
        def i
        for (i = 0; i < mStack.size(); i++){
            if(mStack[i].sameLocation(another)) break
        }
        return (i < mStack.size())
        // if (i < mStack.size()) mStack = mStack[0..i]
    }

    def locationMatch(pair) {
        return (!isEmpty() && pairPeek().sameLocation(pair))
    }
}
