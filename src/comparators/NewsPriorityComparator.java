package comparators;

import java.util.Comparator;

import communication.News;

public class NewsPriorityComparator implements Comparator<News> {
    @Override
    public int compare(News o1, News o2) {
        if (o1.isPinned() == o2.isPinned()) {
            return o1.getTitle().compareToIgnoreCase(o2.getTitle());
        }
        return Boolean.compare(o2.isPinned(), o1.isPinned());
    }
}
