INSERT INTO DIRECTORY_VIEWS (ID, NAME, COLLECTION, LAYOUT, FIELD, OPERATION_KEY) VALUES (1, 'People', 'persons', 'LIST', 'name', 'STARTS_WITH');
INSERT INTO DIRECTORY_VIEWS (ID, NAME, COLLECTION, LAYOUT, FIELD, OPERATION_KEY) VALUES (2, 'Organizations', 'organizations', 'LIST', 'name', 'STARTS_WITH');
INSERT INTO DIRECTORY_VIEWS (ID, NAME, COLLECTION, LAYOUT, FIELD, OPERATION_KEY) VALUES (3, 'Research', 'documents', 'LIST', 'title', 'STARTS_WITH');

INSERT INTO DIRECTORY_VIEW_TEMPLATES (DIRECTORY_VIEW_ID, KEY, TEMPLATE) VALUES (1, 'default', '<div style="display: flex"><div style="margin-right: 25px"><div>{{#thumbnail}}<img src="http://scholars.library.tamu.edu/vivo{{thumbnail}}" style="border-radius: 50%" height="60" width="60">{{/thumbnail}}{{^thumbnail}}<img src="assets/images/default-avatar.png" style="border-radius: 50%" height="60" width="60">{{/thumbnail}}</div></div><div style="margin: auto 0"><div><a href>{{name}}</a></div><div><span>{{preferredTitle}}</span></div></div></div>');
INSERT INTO DIRECTORY_VIEW_TEMPLATES (DIRECTORY_VIEW_ID, KEY, TEMPLATE) VALUES (2, 'default', '<div><div><a href>{{name}}</a></div><div><span>{{type}}</span></div></div>');
INSERT INTO DIRECTORY_VIEW_TEMPLATES (DIRECTORY_VIEW_ID, KEY, TEMPLATE) VALUES (3, 'default', '<div><a href>{{title}}</a></div>');

INSERT INTO DIRECTORY_VIEW_FACETS (DIRECTORY_VIEW_ID, NAME, FIELD, DEFAULT_LIMIT, HIDDEN, SORT, DIRECTION) VALUES (1, 'Type', 'type', 10, false, 'COUNT', 'DESC');
INSERT INTO DIRECTORY_VIEW_FACETS (DIRECTORY_VIEW_ID, NAME, FIELD, DEFAULT_LIMIT, HIDDEN, SORT, DIRECTION) VALUES (2, 'Type', 'type', 10, false, 'COUNT', 'DESC');
INSERT INTO DIRECTORY_VIEW_FACETS (DIRECTORY_VIEW_ID, NAME, FIELD, DEFAULT_LIMIT, HIDDEN, SORT, DIRECTION) VALUES (3, 'Type', 'type', 10, false, 'COUNT', 'DESC');

INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (1, 'A');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (1, 'B');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (1, 'C');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (1, 'D');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (1, 'E');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (1, 'F');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (1, 'G');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (1, 'H');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (1, 'I');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (1, 'J');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (1, 'K');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (1, 'L');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (1, 'M');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (1, 'N');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (1, 'O');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (1, 'P');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (1, 'Q');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (1, 'R');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (1, 'S');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (1, 'T');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (1, 'U');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (1, 'V');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (1, 'W');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (1, 'X');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (1, 'Y');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (1, 'Z');

INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (2, 'A');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (2, 'B');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (2, 'C');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (2, 'D');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (2, 'E');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (2, 'F');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (2, 'G');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (2, 'H');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (2, 'I');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (2, 'J');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (2, 'K');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (2, 'L');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (2, 'M');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (2, 'N');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (2, 'O');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (2, 'P');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (2, 'Q');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (2, 'R');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (2, 'S');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (2, 'T');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (2, 'U');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (2, 'V');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (2, 'W');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (2, 'X');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (2, 'Y');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (2, 'Z');

INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (3, 'A');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (3, 'B');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (3, 'C');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (3, 'D');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (3, 'E');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (3, 'F');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (3, 'G');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (3, 'H');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (3, 'I');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (3, 'J');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (3, 'K');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (3, 'L');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (3, 'M');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (3, 'N');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (3, 'O');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (3, 'P');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (3, 'Q');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (3, 'R');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (3, 'S');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (3, 'T');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (3, 'U');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (3, 'V');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (3, 'W');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (3, 'X');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (3, 'Y');
INSERT INTO DIRECTORY_VIEW_OPTIONS (DIRECTORY_VIEW_ID, OPTIONS) VALUES (3, 'Z');


INSERT INTO DISCOVERY_VIEWS (ID, NAME, COLLECTION, LAYOUT) VALUES (1, 'People', 'persons', 'GRID');
INSERT INTO DISCOVERY_VIEWS (ID, NAME, COLLECTION, LAYOUT) VALUES (2, 'Publications', 'documents', 'LIST');
INSERT INTO DISCOVERY_VIEWS (ID, NAME, COLLECTION, LAYOUT) VALUES (3, 'Grants', 'relationships', 'LIST');
INSERT INTO DISCOVERY_VIEWS (ID, NAME, COLLECTION, LAYOUT) VALUES (4, 'Awards', 'relationships', 'LIST');
INSERT INTO DISCOVERY_VIEWS (ID, NAME, COLLECTION, LAYOUT) VALUES (5, 'Courses', 'processes', 'LIST');
INSERT INTO DISCOVERY_VIEWS (ID, NAME, COLLECTION, LAYOUT) VALUES (6, 'Concepts', 'concepts', 'LIST');

INSERT INTO DISCOVERY_VIEW_TEMPLATES (DISCOVERY_VIEW_ID, KEY, TEMPLATE) VALUES (1, 'default', '<div style="display: flex"> <div style="margin-right: 25px"> <div>{{#thumbnail}}<img src="http://scholars.library.tamu.edu/vivo{{thumbnail}}" style="border-radius: 50%" height="60" width="60">{{/thumbnail}}{{^thumbnail}}<img src="assets/images/default-avatar.png" style="border-radius: 50%" height="60" width="60">{{/thumbnail}}</div> </div> <div style="margin: auto 0"> <div style="font-size: 15px"><a href>{{name}}</a></div> <div style="margin-top: 10px"><span>{{preferredTitle}}</span></div> <div class="comma-seperated"> {{#positionOrganization}} <span><a href>{{.}}</a></span> {{/positionOrganization}} </div> {{#researchArea.length}} <div style="margin-top: 10px"><span>Research Areas</span></div> <div class="comma-seperated"> {{#researchArea}} <span><a href>{{.}}</a></span> {{/researchArea}} </div> {{/researchArea.length}} </div></div><div style="display: flex; margin-top: 10px;"> <div style="margin: auto"> <a href style="color: black; text-decoration: none">Co-Author Network</a> </div> <div style="margin: auto"> <a href style="color: black; text-decoration: none">Map of Science</a> </div></div><style> .comma-seperated span:after { content: ", "; } .comma-seperated span:last-child:after { content: ""; }</style>');
INSERT INTO DISCOVERY_VIEW_TEMPLATES (DISCOVERY_VIEW_ID, KEY, TEMPLATE) VALUES (2, 'default', '<div style="display: flex"> <span>{{title}}</span> <span style="margin-left: 5px">|</span> <span style="margin-left: 5px">{{type}}</span></div><div style="display: flex; margin-top: 10px;"> {{#doi}} <span style="margin-right: 5px"> <a href="http://dx.doi.org/{{doi}}"><img src="assets/images/doi-badge.jpg" width="100" /></a> </span> {{/doi}} {{#pmid}} <span style="margin-right: 5px"> <a href="https://www.ncbi.nlm.nih.gov/pubmed/{{pmid}}"><img src="assets/images/pubmed-badge.jpg" width="100" /></a> </span> {{/pmid}} {{#uri}} <span style="margin-right: 5px"> <a href="{{uri}}"><img src="assets/images/open-access-badge.jpg" width="100" /></a> </span> {{^doi}} <span style="margin-right: 5px" class="altmetric-embed" data-hide-no-mentions="true" data-badge-popover="top" data-handle="{{uri}}"></span> {{/doi}} {{/uri}} {{#doi}} <span style="margin-right: 5px" class="altmetric-embed" data-hide-no-mentions="true" data-badge-popover="top" data-doi="{{doi}}"></span> <span style="margin-right: 5px" class="__dimensions_badge_embed__" data-hide-zero-citations="true" data-style="small_rectangle" data-legend="hover-top" data-doi="{{doi}}"></span> {{/doi}}</div>');
INSERT INTO DISCOVERY_VIEW_TEMPLATES (DISCOVERY_VIEW_ID, KEY, TEMPLATE) VALUES (3, 'default', '<span>{{title}}</span>');
INSERT INTO DISCOVERY_VIEW_TEMPLATES (DISCOVERY_VIEW_ID, KEY, TEMPLATE) VALUES (4, 'default', '<span>{{title}}</span>');
INSERT INTO DISCOVERY_VIEW_TEMPLATES (DISCOVERY_VIEW_ID, KEY, TEMPLATE) VALUES (5, 'default', '<span>{{title}}</span>');
INSERT INTO DISCOVERY_VIEW_TEMPLATES (DISCOVERY_VIEW_ID, KEY, TEMPLATE) VALUES (6, 'default', '<span>{{name}}</span>');

INSERT INTO DISCOVERY_VIEW_FILTERS (DISCOVERY_VIEW_ID, FIELD, VALUE) VALUES (3, 'type', 'Grant');
INSERT INTO DISCOVERY_VIEW_FILTERS (DISCOVERY_VIEW_ID, FIELD, VALUE) VALUES (4, 'type', 'AwardReceipt');
INSERT INTO DISCOVERY_VIEW_FILTERS (DISCOVERY_VIEW_ID, FIELD, VALUE) VALUES (5, 'type', 'Course');

INSERT INTO DISCOVERY_VIEW_FACETS (DISCOVERY_VIEW_ID, NAME, FIELD, DEFAULT_LIMIT, HIDDEN, SORT, DIRECTION) VALUES (1, 'People', 'type', 10, false, 'COUNT', 'DESC');
INSERT INTO DISCOVERY_VIEW_FACETS (DISCOVERY_VIEW_ID, NAME, FIELD, DEFAULT_LIMIT, HIDDEN, SORT, DIRECTION) VALUES (1, 'Organization', 'positionOrganization', 10, false, 'COUNT', 'DESC');
INSERT INTO DISCOVERY_VIEW_FACETS (DISCOVERY_VIEW_ID, NAME, FIELD, DEFAULT_LIMIT, HIDDEN, SORT, DIRECTION) VALUES (1, 'Research Area', 'researchArea', 10, false, 'COUNT', 'DESC');

INSERT INTO DISCOVERY_VIEW_FACETS (DISCOVERY_VIEW_ID, NAME, FIELD, DEFAULT_LIMIT, HIDDEN, SORT, DIRECTION) VALUES (2, 'Publications', 'type', 10, false, 'COUNT', 'DESC');
INSERT INTO DISCOVERY_VIEW_FACETS (DISCOVERY_VIEW_ID, NAME, FIELD, DEFAULT_LIMIT, HIDDEN, SORT, DIRECTION) VALUES (2, 'Date Published', 'publicationDate', 10, false, 'COUNT', 'DESC');

INSERT INTO DISCOVERY_VIEW_FACETS (DISCOVERY_VIEW_ID, NAME, FIELD, DEFAULT_LIMIT, HIDDEN, SORT, DIRECTION) VALUES (3, 'Grants', 'type', 10, true, 'COUNT', 'DESC');
INSERT INTO DISCOVERY_VIEW_FACETS (DISCOVERY_VIEW_ID, NAME, FIELD, DEFAULT_LIMIT, HIDDEN, SORT, DIRECTION) VALUES (3, 'Date Start', 'dateTimeIntervalStart', 10, false, 'COUNT', 'DESC');
INSERT INTO DISCOVERY_VIEW_FACETS (DISCOVERY_VIEW_ID, NAME, FIELD, DEFAULT_LIMIT, HIDDEN, SORT, DIRECTION) VALUES (3, 'Date End', 'dateTimeIntervalEnd', 10, false, 'COUNT', 'DESC');

INSERT INTO DISCOVERY_VIEW_FACETS (DISCOVERY_VIEW_ID, NAME, FIELD, DEFAULT_LIMIT, HIDDEN, SORT, DIRECTION) VALUES (4, 'Awards', 'type', 10, true, 'COUNT', 'DESC');
INSERT INTO DISCOVERY_VIEW_FACETS (DISCOVERY_VIEW_ID, NAME, FIELD, DEFAULT_LIMIT, HIDDEN, SORT, DIRECTION) VALUES (4, 'Award Or Honor For', 'awardOrHonorFor', 10, false, 'COUNT', 'DESC');
INSERT INTO DISCOVERY_VIEW_FACETS (DISCOVERY_VIEW_ID, NAME, FIELD, DEFAULT_LIMIT, HIDDEN, SORT, DIRECTION) VALUES (4, 'Award Conferred By', 'awardConferredBy', 10, false, 'COUNT', 'DESC');

INSERT INTO DISCOVERY_VIEW_FACETS (DISCOVERY_VIEW_ID, NAME, FIELD, DEFAULT_LIMIT, HIDDEN, SORT, DIRECTION) VALUES (5, 'Courses', 'type', 10, true, 'COUNT', 'DESC');
INSERT INTO DISCOVERY_VIEW_FACETS (DISCOVERY_VIEW_ID, NAME, FIELD, DEFAULT_LIMIT, HIDDEN, SORT, DIRECTION) VALUES (5, 'Offered By', 'offeredBy', 10, false, 'COUNT', 'DESC');
INSERT INTO DISCOVERY_VIEW_FACETS (DISCOVERY_VIEW_ID, NAME, FIELD, DEFAULT_LIMIT, HIDDEN, SORT, DIRECTION) VALUES (5, 'Date Start', 'dateTimeIntervalStart', 10, false, 'COUNT', 'DESC');
INSERT INTO DISCOVERY_VIEW_FACETS (DISCOVERY_VIEW_ID, NAME, FIELD, DEFAULT_LIMIT, HIDDEN, SORT, DIRECTION) VALUES (5, 'Date End', 'dateTimeIntervalEnd', 10, false, 'COUNT', 'DESC');

INSERT INTO DISCOVERY_VIEW_FACETS (DISCOVERY_VIEW_ID, NAME, FIELD, DEFAULT_LIMIT, HIDDEN, SORT, DIRECTION) VALUES (6, 'Concepts', 'type', 10, true, 'COUNT', 'DESC');
