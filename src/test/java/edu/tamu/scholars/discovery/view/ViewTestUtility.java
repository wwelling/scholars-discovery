package edu.tamu.scholars.discovery.view;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.data.domain.Sort.Direction;

import edu.tamu.scholars.discovery.model.OpKey;
import edu.tamu.scholars.discovery.view.model.Boost;
import edu.tamu.scholars.discovery.view.model.ContainerType;
import edu.tamu.scholars.discovery.view.model.DataAndAnalyticsView;
import edu.tamu.scholars.discovery.view.model.DirectoryView;
import edu.tamu.scholars.discovery.view.model.DiscoveryView;
import edu.tamu.scholars.discovery.view.model.DisplaySectionView;
import edu.tamu.scholars.discovery.view.model.DisplaySubsectionView;
import edu.tamu.scholars.discovery.view.model.DisplayTabView;
import edu.tamu.scholars.discovery.view.model.DisplayView;
import edu.tamu.scholars.discovery.view.model.ExportField;
import edu.tamu.scholars.discovery.view.model.ExportFieldView;
import edu.tamu.scholars.discovery.view.model.ExportView;
import edu.tamu.scholars.discovery.view.model.Facet;
import edu.tamu.scholars.discovery.view.model.FacetSort;
import edu.tamu.scholars.discovery.view.model.FacetType;
import edu.tamu.scholars.discovery.view.model.Filter;
import edu.tamu.scholars.discovery.view.model.Grouping;
import edu.tamu.scholars.discovery.view.model.Layout;
import edu.tamu.scholars.discovery.view.model.Side;
import edu.tamu.scholars.discovery.view.model.Sort;

public class ViewTestUtility {

    public static final String MOCK_VIEW_NAME = "View";

    public static DataAndAnalyticsView getMockDataAndAnalyticsView() {
        DataAndAnalyticsView dataAndAnalyticsView = new DataAndAnalyticsView();

        dataAndAnalyticsView.setName(MOCK_VIEW_NAME);
        dataAndAnalyticsView.setLayout(Layout.GRID);
        dataAndAnalyticsView.setType(ContainerType.PROFILE_SUMMARIES_EXPORT);

        Map<String, String> templates = new HashMap<String, String>();
        templates.put("default", "<h1>Element template from WSYWIG</h1>");

        dataAndAnalyticsView.setTemplates(templates);

        List<String> styles = new ArrayList<String>();

        styles.add("color: maroon;");

        dataAndAnalyticsView.setStyles(styles);

        List<String> fields = new ArrayList<String>();

        fields.add("title");

        dataAndAnalyticsView.setFields(fields);

        List<Facet> facets = new ArrayList<Facet>();

        Facet facet = new Facet();

        facet.setName("Name");
        facet.setField("name");
        facet.setType(FacetType.STRING);
        facet.setSort(FacetSort.COUNT);
        facet.setDirection(Direction.DESC);
        facet.setPageSize(20);
        facet.setPageNumber(1);

        facets.add(facet);

        dataAndAnalyticsView.setFacets(facets);

        List<Filter> filters = new ArrayList<Filter>();

        Filter filter = new Filter();

        filter.setField("type");
        filter.setValue("FacultyMember");

        filters.add(filter);

        dataAndAnalyticsView.setFilters(filters);

        List<Boost> boosts = new ArrayList<Boost>();

        Boost boost = new Boost();

        boost.setField("name");
        boost.setValue(2.0f);

        boosts.add(boost);

        dataAndAnalyticsView.setBoosts(boosts);

        List<Sort> sorting = new ArrayList<Sort>();

        Sort sort = new Sort();
        sort.setField("name");
        sort.setDirection(Direction.ASC);

        sorting.add(sort);

        dataAndAnalyticsView.setSort(sorting);

        List<ExportField> exporting = new ArrayList<ExportField>();

        ExportField idExport = new ExportField();

        idExport.setColumnHeader("Id");
        idExport.setValuePath("id");

        exporting.add(idExport);

        ExportField nameExport = new ExportField();

        nameExport.setColumnHeader("Name");
        nameExport.setValuePath("name");

        exporting.add(nameExport);

        dataAndAnalyticsView.setExport(exporting);

        return dataAndAnalyticsView;
    }

    public static DirectoryView getMockDirectoryView() {
        DirectoryView directoryView = new DirectoryView();

        directoryView.setName(MOCK_VIEW_NAME);
        directoryView.setLayout(Layout.LIST);

        Map<String, String> templates = new HashMap<String, String>();
        templates.put("default", "<h1>Person template from WSYWIG</h1>");

        directoryView.setTemplates(templates);

        List<String> styles = new ArrayList<String>();

        styles.add("color: maroon;");

        directoryView.setStyles(styles);

        List<String> fields = new ArrayList<String>();

        fields.add("title");

        directoryView.setFields(fields);

        List<Facet> facets = new ArrayList<Facet>();

        Facet facet = new Facet();

        facet.setName("Name");
        facet.setField("name");
        facet.setType(FacetType.STRING);
        facet.setSort(FacetSort.COUNT);
        facet.setDirection(Direction.DESC);
        facet.setPageSize(20);
        facet.setPageNumber(1);

        facets.add(facet);

        directoryView.setFacets(facets);

        List<Filter> filters = new ArrayList<Filter>();

        Filter filter = new Filter();

        filter.setField("type");
        filter.setValue("FacultyMember");

        filters.add(filter);

        directoryView.setFilters(filters);

        List<Boost> boosts = new ArrayList<Boost>();

        Boost boost = new Boost();

        boost.setField("name");
        boost.setValue(2.0f);

        boosts.add(boost);

        directoryView.setBoosts(boosts);

        List<Sort> sorting = new ArrayList<Sort>();

        Sort sort = new Sort();
        sort.setField("name");
        sort.setDirection(Direction.ASC);

        sorting.add(sort);

        directoryView.setSort(sorting);

        Grouping grouping = new Grouping();

        grouping.setField("name");
        grouping.setOpKey(OpKey.ENDS_WITH);

        directoryView.setGrouping(grouping);

        List<ExportField> exporting = new ArrayList<ExportField>();

        ExportField idExport = new ExportField();

        idExport.setColumnHeader("Id");
        idExport.setValuePath("id");

        exporting.add(idExport);

        ExportField nameExport = new ExportField();

        nameExport.setColumnHeader("Name");
        nameExport.setValuePath("name");

        exporting.add(nameExport);

        directoryView.setExport(exporting);

        return directoryView;
    }

    public static DiscoveryView getMockDiscoveryView() {
        DiscoveryView discoveryView = new DiscoveryView();

        discoveryView.setName(MOCK_VIEW_NAME);
        discoveryView.setLayout(Layout.GRID);
        discoveryView.setDefaultSearchField("overview");

        List<String> highlightFields = new ArrayList<String>();
        highlightFields.add("overview");
        discoveryView.setHighlightFields(highlightFields);
        discoveryView.setHighlightPrefix("<em>");
        discoveryView.setHighlightPostfix("</em>");

        Map<String, String> templates = new HashMap<String, String>();
        templates.put("default", "<h1>Person template from WSYWIG</h1>");

        discoveryView.setTemplates(templates);

        List<String> styles = new ArrayList<String>();

        styles.add("color: maroon;");

        discoveryView.setStyles(styles);

        List<String> fields = new ArrayList<String>();

        fields.add("title");

        discoveryView.setFields(fields);

        List<Facet> facets = new ArrayList<Facet>();

        Facet facet = new Facet();

        facet.setName("Name");
        facet.setField("name");
        facet.setType(FacetType.STRING);
        facet.setSort(FacetSort.COUNT);
        facet.setDirection(Direction.DESC);
        facet.setPageSize(20);
        facet.setPageNumber(1);

        facets.add(facet);

        discoveryView.setFacets(facets);

        List<Filter> filters = new ArrayList<Filter>();

        Filter filter = new Filter();

        filter.setField("type");
        filter.setValue("FacultyMember");

        filters.add(filter);

        discoveryView.setFilters(filters);

        List<Boost> boosts = new ArrayList<Boost>();

        Boost boost = new Boost();

        boost.setField("name");
        boost.setValue(2.0f);

        boosts.add(boost);

        discoveryView.setBoosts(boosts);

        List<Sort> sorting = new ArrayList<Sort>();

        Sort sort = new Sort();
        sort.setField("name");
        sort.setDirection(Direction.ASC);

        sorting.add(sort);

        discoveryView.setSort(sorting);

        List<ExportField> exporting = new ArrayList<ExportField>();

        ExportField idExport = new ExportField();

        idExport.setColumnHeader("Id");
        idExport.setValuePath("id");

        exporting.add(idExport);

        ExportField nameExport = new ExportField();

        nameExport.setColumnHeader("Name");
        nameExport.setValuePath("name");

        exporting.add(nameExport);

        discoveryView.setExport(exporting);

        return discoveryView;
    }

    public static DisplayView getMockDisplayView() {
        DisplayView displayView = new DisplayView();

        displayView.setName(MOCK_VIEW_NAME);
        displayView.setMainContentTemplate("<div>Main</div>");
        displayView.setMainContentTemplate("<div>Main</div>");
        displayView.setLeftScanTemplate("<div>Left Scan</div>");
        displayView.setRightScanTemplate("<div>Right Scan</div>");
        displayView.setAsideTemplate("<div>Aside</div>");
        displayView.setAsideLocation(Side.RIGHT);

        List<String> types = new ArrayList<String>();
        types.add("FacultyMember");

        displayView.setTypes(types);

        Map<String, String> metaTemplates = new HashMap<String, String>();
        metaTemplates.put("default", "Meta tag template");

        displayView.setMetaTemplates(metaTemplates);

        Map<String, String> embedTemplates = new HashMap<String, String>();
        embedTemplates.put("default", "<div>Hello, Embedded!</div>");

        displayView.setEmbedTemplates(embedTemplates);

        List<DisplayTabView> tabs = new ArrayList<DisplayTabView>();

        tabs.add(getMockDisplayTabView());

        displayView.setTabs(tabs);

        displayView.setExportViews(getMockExportViews());

        return displayView;
    }

    public static DisplayTabView getMockDisplayTabView() {
        DisplayTabView tab = new DisplayTabView();
        tab.setName("Test");

        List<DisplaySectionView> sections = new ArrayList<DisplaySectionView>();

        DisplaySectionView section = new DisplaySectionView();
        section.setName("Test");
        section.setTemplate("<span>Hello, World!</span>");

        section.setField("name");
        section.setOrder(1);

        Filter sectionFilter = new Filter();
        sectionFilter.setField("type");
        sectionFilter.setValue("Test");

        Set<Filter> sectionFilters = new HashSet<Filter>();
        sectionFilters.add(sectionFilter);

        section.setFilters(sectionFilters);

        Sort sectionSort = new Sort();
        sectionSort.setField("name");
        sectionSort.setDirection(Direction.ASC);

        Set<Sort> sectionSorting = new HashSet<Sort>();
        sectionSorting.add(sectionSort);

        section.setSort(sectionSorting);

        List<String> requiredFields = new ArrayList<String>();
        requiredFields.add("type");

        section.setRequiredFields(requiredFields);

        List<String> lazyReferences = new ArrayList<String>();
        lazyReferences.add("publications");

        section.setLazyReferences(lazyReferences);

        DisplaySubsectionView subsection = new DisplaySubsectionView();

        subsection.setName("Test");
        subsection.setField("publications");
        subsection.setOrder(1);

        Filter subsectionFilter = new Filter();
        subsectionFilter.setField("type");
        subsectionFilter.setValue("Test");

        Set<Filter> subsectionFilters = new HashSet<Filter>();
        subsectionFilters.add(subsectionFilter);

        subsection.setFilters(subsectionFilters);

        Sort subsectionSort = new Sort();
        subsectionSort.setField("date");
        subsectionSort.setDirection(Direction.DESC);

        Set<Sort> subsectionSorting = new HashSet<Sort>();
        subsectionSorting.add(subsectionSort);

        subsection.setSort(subsectionSorting);

        subsection.setTemplate("<div>Subsection</div>");

        List<DisplaySubsectionView> subsections = new ArrayList<DisplaySubsectionView>();
        subsections.add(subsection);

        section.setSubsections(subsections);

        sections.add(section);

        tab.setSections(sections);

        return tab;
    }

    public static List<ExportView> getMockExportViews() {
        List<ExportView> expoerViews = new ArrayList<>();
        ExportView exportView = new ExportView();

        exportView.setName("Test");

        exportView.setContentTemplate("<html><body><span>Hello, Content!</span></body></html>");
        exportView.setHeaderTemplate("<html><body><span>Hello, Header!</span></body></html>");

        Set<ExportFieldView> fieldViews = new HashSet<ExportFieldView>();

        ExportFieldView exportField = new ExportFieldView();

        exportField.setName("Test");
        exportField.setField("publications");
        exportField.setOrder(1);

        Filter filter = new Filter();
        filter.setField("type");
        filter.setValue("Test");

        Set<Filter> filters = new HashSet<Filter>();
        filters.add(filter);

        exportField.setFilters(filters);

        Sort sort = new Sort();
        sort.setField("date");
        sort.setDirection(Direction.DESC);
        sort.setDate(true);

        Set<Sort> sorting = new HashSet<Sort>();
        sorting.add(sort);

        exportField.setSort(sorting);

        exportField.setLimit(10);

        fieldViews.add(exportField);

        exportView.setLazyReferences(fieldViews);

        expoerViews.add(exportView);

        return expoerViews;
    }

}
