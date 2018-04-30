package com.consort.kubernetesadapter.restmodel;

public class SuccessResult<T> {

    T resultData;

    public T getResultData() {
        return resultData;
    }

    public void setResultData(final T resultData) {
        this.resultData = resultData;
    }
}
