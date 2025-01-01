package com.godpalace.gamegl.test;

import com.godpalace.gamegl.protection.ProcessProtection;

public class ProcessProtectionTest  {
    public static void main(String[] args) {
        int pid = ProcessProtection.getProcessId("notepad.exe");

        ProcessProtection protection
                = new ProcessProtection(pid, "C:\\Windows\\System32\\notepad.exe");
        protection.start();
    }
}
