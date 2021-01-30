package com.anypli.megamall.contracts;

import android.os.Bundle;

public class CommonContract {
    public interface ViewItf{
        void dealWithIt(Bundle input);
        void dealWithIt(Object input);
    }
    public interface PresenterItf{
        void dealWithIt(Bundle input);
        void dealWithIt(Object input);
    }
}
