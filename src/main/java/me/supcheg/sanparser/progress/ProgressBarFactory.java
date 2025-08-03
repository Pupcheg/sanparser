package me.supcheg.sanparser.progress;

import me.tongfei.progressbar.ProgressBar;

public interface ProgressBarFactory {
    ProgressBar createProgressBar(String title);
}
