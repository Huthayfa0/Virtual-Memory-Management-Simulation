package com.main.virtualmemorymanagementsimulation;

import java.util.Objects;

public class Frame {
    int pid;
    int page;

    public Frame(int pid, int page) {
        this.pid = pid;
        this.page = page;
    }

    public int getPid() {
        return pid;
    }

    public int getPage() {
        return page;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Frame frame)) return false;
        return getPid() == frame.getPid() && getPage() == frame.getPage();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPid(), getPage());
    }
}
