package com.fireflylearning.tasksummary.objects;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Ben on 14/07/2017.
 */

public class Task {
    public int id;
    public String title;
    public String description_page_url;
    public Date set_date = new Date();
    public Date due_date = new Date();
    public Boolean archived;
    public Boolean draft;
    public Boolean show_in_markbook;
    public Boolean highlight_in_markbook;
    public Boolean show_in_parent_portal;
    public Boolean hide_addressees;

    public Task(int id, String title, String description_page_url, Date set_date, Date due_date, Boolean archived, Boolean draft, Boolean show_in_markbook, Boolean highlight_in_markbook, Boolean show_in_parent_portal, Boolean hide_addressees) {
        this.id = id;
        this.title = title;
        this.description_page_url = description_page_url;
        this.set_date = set_date;
        this.due_date = due_date;
        this.archived = archived;
        this.draft = draft;
        this.show_in_markbook = show_in_markbook;
        this.highlight_in_markbook = highlight_in_markbook;
        this.show_in_parent_portal = show_in_parent_portal;
        this.hide_addressees = hide_addressees;
    }

    public String toFlagsString() {
        ArrayList<String> flags = new ArrayList<>();

        if (archived)
            flags.add("archived");

        if (draft)
            flags.add("draft");

        if (show_in_markbook && highlight_in_markbook)
            flags.add("markbook (highlighted)");
        else if (show_in_markbook)
            flags.add("markbook");

        if (show_in_parent_portal)
            flags.add("parents");

        if (hide_addressees)
            flags.add("no addressees");

        return android.text.TextUtils.join(", ", flags);
    }
}
