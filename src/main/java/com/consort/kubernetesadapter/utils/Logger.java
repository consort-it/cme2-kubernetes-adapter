package com.consort.kubernetesadapter.utils;

public class Logger {
  static public void logInfo(String msg) {
    System.out.println(msg);
  }
  static public void logErr(String msg) {
    System.err.println(msg);
  }
}
