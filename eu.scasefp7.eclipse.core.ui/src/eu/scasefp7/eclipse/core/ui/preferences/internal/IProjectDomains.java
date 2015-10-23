/**
 * Copyright 2015 S-CASE Consortium
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eu.scasefp7.eclipse.core.ui.preferences.internal;

/**
 * @author emaorli
 *
 */
public interface IProjectDomains {
	/**
	 * Project domains from Google Verticals
	 */
	public static final DomainEntry[] PROJECT_DOMAINS = {
		
		// Generated in a spreadsheet
		new DomainEntry(3, "Arts & Entertainment", new DomainEntry[] { 
			new DomainEntry(184, "Celebrities & Entertainment News"),
			new DomainEntry(316, "Comics & Animation"),
			new DomainEntry(612, "Entertainment Industry"),
			new DomainEntry(569, "Events & Listings"),
			new DomainEntry(539, "Fun & Trivia"),
			new DomainEntry(182, "Humor"),
			new DomainEntry(34, "Movies"),
			new DomainEntry(35, "Music & Audio"),
			new DomainEntry(33, "Offbeat"),
			new DomainEntry(613, "Online Media"),
			new DomainEntry(23, "Performing Arts"),
			new DomainEntry(36, "TV & Video"),
			new DomainEntry(24, "Visual Art & Design"),
		}),
		new DomainEntry(5, "Computers & Electronics", new DomainEntry[] { 
			new DomainEntry(1300, "CAD & CAM"),
			new DomainEntry(30, "Computer Hardware"),
			new DomainEntry(314, "Computer Security"),
			new DomainEntry(78, "Consumer Electronics"),
			new DomainEntry(434, "Electronics & Electrical"),
			new DomainEntry(77, "Enterprise Technology"),
			new DomainEntry(311, "Networking"),
			new DomainEntry(31, "Programming"),
			new DomainEntry(32, "Software"),
		}),
		new DomainEntry(7, "Finance", new DomainEntry[] { 
			new DomainEntry(278, "Accounting & Auditing"),
			new DomainEntry(37, "Banking"),
			new DomainEntry(279, "Credit & Lending"),
			new DomainEntry(903, "Financial Planning & Management"),
			new DomainEntry(1282, "Grants, Scholarships & Financial Aid"),
			new DomainEntry(38, "Insurance"),
			new DomainEntry(107, "Investing"),
		}),
		new DomainEntry(8, "Games", new DomainEntry[] { 
			new DomainEntry(919, "Arcade & Coin-Op Games"),
			new DomainEntry(920, "Board Games"),
			new DomainEntry(39, "Card Games"),
			new DomainEntry(41, "Computer & Video Games"),
			new DomainEntry(1492, "Dice Games"),
			new DomainEntry(1493, "Educational Games"),
			new DomainEntry(1290, "Family-Oriented Games & Activities"),
			new DomainEntry(637, "Gambling"),
			new DomainEntry(105, "Online Games"),
			new DomainEntry(936, "Party Games"),
			new DomainEntry(937, "Puzzles & Brainteasers"),
			new DomainEntry(622, "Roleplaying Games"),
			new DomainEntry(938, "Table Games"),
			new DomainEntry(1549, "Tile Games"),
			new DomainEntry(1494, "Word Games"),
		}),
		new DomainEntry(11, "Home & Garden", new DomainEntry[] { 
			new DomainEntry(948, "Bed & Bath"),
			new DomainEntry(472, "Domestic Services"),
			new DomainEntry(269, "Gardening & Landscaping"),
			new DomainEntry(271, "Home Appliances"),
			new DomainEntry(270, "Home Furnishings"),
			new DomainEntry(158, "Home Improvement"),
			new DomainEntry(1348, "Home Storage & Shelving"),
			new DomainEntry(137, "Homemaking & Interior Decor"),
			new DomainEntry(828, "HVAC & Climate Control"),
			new DomainEntry(951, "Kitchen & Dining"),
			new DomainEntry(1364, "Laundry"),
			new DomainEntry(1372, "Nursery & Playroom"),
			new DomainEntry(471, "Pest Control"),
			new DomainEntry(952, "Swimming Pools & Spas"),
			new DomainEntry(953, "Yard & Patio"),
		}),
		new DomainEntry(12, "Business & Industrial", new DomainEntry[] { 
			new DomainEntry(25, "Advertising & Marketing"),
			new DomainEntry(356, "Aerospace & Defense"),
			new DomainEntry(46, "Agriculture & Forestry"),
			new DomainEntry(1190, "Automotive Industry"),
			new DomainEntry(799, "Business Education"),
			new DomainEntry(1138, "Business Finance"),
			new DomainEntry(1159, "Business Operations"),
			new DomainEntry(329, "Business Services"),
			new DomainEntry(288, "Chemicals Industry"),
			new DomainEntry(48, "Construction & Maintenance"),
			new DomainEntry(233, "Energy & Utilities"),
			new DomainEntry(955, "Hospitality Industry"),
			new DomainEntry(287, "Industrial Materials & Equipment"),
			new DomainEntry(49, "Manufacturing"),
			new DomainEntry(606, "Metals & Mining"),
			new DomainEntry(255, "Pharmaceuticals & Biotech"),
			new DomainEntry(1176, "Printing & Publishing"),
			new DomainEntry(1199, "Professional & Trade Associations"),
			new DomainEntry(841, "Retail Trade"),
			new DomainEntry(551, "Small Business"),
			new DomainEntry(566, "Textiles & Nonwovens"),
			new DomainEntry(50, "Transportation & Logistics"),
		}),
		new DomainEntry(13, "Internet & Telecom", new DomainEntry[] { 
			new DomainEntry(385, "Communications Equipment"),
			new DomainEntry(394, "Email & Messaging"),
			new DomainEntry(382, "Mobile & Wireless"),
			new DomainEntry(485, "Search Engines"),
			new DomainEntry(383, "Service Providers"),
			new DomainEntry(392, "Teleconferencing"),
			new DomainEntry(1142, "Web Apps & Online Tools"),
			new DomainEntry(301, "Web Portals"),
			new DomainEntry(302, "Web Services"),
		}),
		new DomainEntry(14, "People & Society", new DomainEntry[] { 
			new DomainEntry(677, "Disabled & Special Needs"),
			new DomainEntry(56, "Ethnic & Identity Groups"),
			new DomainEntry(1131, "Family & Relationships"),
			new DomainEntry(594, "Men's Interests"),
			new DomainEntry(59, "Religion & Belief"),
			new DomainEntry(298, "Seniors & Retirement"),
			new DomainEntry(54, "Social Issues & Advocacy"),
			new DomainEntry(509, "Social Sciences"),
			new DomainEntry(502, "Subcultures & Niche Interests"),
			new DomainEntry(325, "Women's Interests"),
		}),
		new DomainEntry(16, "News", new DomainEntry[] { 
			new DomainEntry(112, "Broadcast & Network News"),
			new DomainEntry(784, "Business News"),
			new DomainEntry(507, "Gossip & Tabloid News"),
			new DomainEntry(1253, "Health News"),
			new DomainEntry(1204, "Journalism & News Industry"),
			new DomainEntry(572, "Local News"),
			new DomainEntry(408, "Newspapers"),
			new DomainEntry(396, "Politics"),
			new DomainEntry(1077, "Sports News"),
			new DomainEntry(785, "Technology News"),
			new DomainEntry(63, "Weather"),
			new DomainEntry(1209, "World News"),
		}),
		new DomainEntry(18, "Shopping", new DomainEntry[] { 
			new DomainEntry(64, "Antiques & Collectibles"),
			new DomainEntry(68, "Apparel"),
			new DomainEntry(292, "Auctions"),
			new DomainEntry(61, "Classifieds"),
			new DomainEntry(69, "Consumer Resources"),
			new DomainEntry(1505, "Discount & Outlet Stores"),
			new DomainEntry(1143, "Entertainment Media"),
			new DomainEntry(70, "Gifts & Special Event Items"),
			new DomainEntry(1507, "Green & Eco-Friendly Shopping"),
			new DomainEntry(696, "Luxury Goods"),
			new DomainEntry(73, "Mass Merchants & Department Stores"),
			new DomainEntry(576, "Photo & Video Services"),
			new DomainEntry(531, "Shopping Portals & Search Engines"),
			new DomainEntry(1210, "Swap Meets & Outdoor Markets"),
			new DomainEntry(123, "Tobacco Products"),
			new DomainEntry(432, "Toys"),
			new DomainEntry(1225, "Wholesalers & Liquidators"),
		}),
		new DomainEntry(19, "Law & Government", new DomainEntry[] { 
			new DomainEntry(76, "Government"),
			new DomainEntry(75, "Legal"),
			new DomainEntry(366, "Military"),
			new DomainEntry(166, "Public Safety"),
			new DomainEntry(508, "Social Services"),
		}),
		new DomainEntry(20, "Sports", new DomainEntry[] { 
			new DomainEntry(1073, "College Sports"),
			new DomainEntry(514, "Combat Sports"),
			new DomainEntry(554, "Extreme Sports"),
			new DomainEntry(998, "Fantasy Sports"),
			new DomainEntry(1000, "Individual Sports"),
			new DomainEntry(180, "Motor Sports"),
			new DomainEntry(1599, "Sport Scores & Statistics"),
			new DomainEntry(263, "Sporting Goods"),
			new DomainEntry(1082, "Sports Coaching & Training"),
			new DomainEntry(1001, "Team Sports"),
			new DomainEntry(118, "Water Sports"),
			new DomainEntry(265, "Winter Sports"),
			new DomainEntry(1198, "World Sports Competitions"),
		}),
		new DomainEntry(22, "Books & Literature", new DomainEntry[] { 
			new DomainEntry(355, "Book Retailers"),
			new DomainEntry(1183, "Children's Literature"),
			new DomainEntry(608, "E-Books"),
			new DomainEntry(540, "Fan Fiction"),
			new DomainEntry(1184, "Literary Classics"),
			new DomainEntry(412, "Magazines"),
			new DomainEntry(565, "Poetry"),
			new DomainEntry(1177, "Writers Resources"),
		}),
		new DomainEntry(29, "Real Estate", new DomainEntry[] { 
			new DomainEntry(378, "Apartments & Residential Rentals"),
			new DomainEntry(1460, "Bank-Owned & Foreclosed Properties"),
			new DomainEntry(1178, "Commercial & Investment Real Estate"),
			new DomainEntry(687, "Property Development"),
			new DomainEntry(463, "Property Inspections & Appraisals"),
			new DomainEntry(425, "Property Management"),
			new DomainEntry(96, "Real Estate Agencies"),
			new DomainEntry(1080, "Real Estate Listings"),
			new DomainEntry(1081, "Timeshares & Vacation Properties"),
		}),
		new DomainEntry(44, "Beauty & Fitness", new DomainEntry[] { 
			new DomainEntry(1219, "Beauty Pageants"),
			new DomainEntry(239, "Body Art"),
			new DomainEntry(1220, "Cosmetic Procedures"),
			new DomainEntry(147, "Cosmetology & Beauty Professionals"),
			new DomainEntry(143, "Face & Body Care"),
			new DomainEntry(185, "Fashion & Style"),
			new DomainEntry(94, "Fitness"),
			new DomainEntry(146, "Hair Care"),
			new DomainEntry(145, "Spas & Beauty Services"),
			new DomainEntry(236, "Weight Loss"),
		}),
		new DomainEntry(45, "Health", new DomainEntry[] { 
			new DomainEntry(623, "Aging & Geriatrics"),
			new DomainEntry(499, "Alternative & Natural Medicine"),
			new DomainEntry(419, "Health Conditions"),
			new DomainEntry(254, "Health Education & Medical Training"),
			new DomainEntry(252, "Health Foundations & Medical Research"),
			new DomainEntry(251, "Medical Devices & Equipment"),
			new DomainEntry(256, "Medical Facilities & Services"),
			new DomainEntry(253, "Medical Literature & Resources"),
			new DomainEntry(636, "Men's Health"),
			new DomainEntry(437, "Mental Health"),
			new DomainEntry(418, "Nursing"),
			new DomainEntry(456, "Nutrition"),
			new DomainEntry(245, "Oral & Dental Care"),
			new DomainEntry(645, "Pediatrics"),
			new DomainEntry(248, "Pharmacy"),
			new DomainEntry(947, "Public Health"),
			new DomainEntry(195, "Reproductive Health"),
			new DomainEntry(257, "Substance Abuse"),
			new DomainEntry(246, "Vision Care"),
			new DomainEntry(648, "Women's Health"),
		}),
		new DomainEntry(47, "Autos & Vehicles", new DomainEntry[] { 
			new DomainEntry(1191, "Bicycles & Accessories"),
			new DomainEntry(1140, "Boats & Watercraft"),
			new DomainEntry(1213, "Campers & RVs"),
			new DomainEntry(1013, "Classic Vehicles"),
			new DomainEntry(1214, "Commercial Vehicles"),
			new DomainEntry(806, "Custom & Performance Vehicles"),
			new DomainEntry(810, "Hybrid & Alternative Vehicles"),
			new DomainEntry(1317, "Microcars & City Cars"),
			new DomainEntry(273, "Motorcycles"),
			new DomainEntry(148, "Off-Road Vehicles"),
			new DomainEntry(1147, "Personal Aircraft"),
			new DomainEntry(1212, "Scooters & Mopeds"),
			new DomainEntry(610, "Trucks & SUVs"),
			new DomainEntry(815, "Vehicle Brands"),
			new DomainEntry(1294, "Vehicle Codes & Driving Laws"),
			new DomainEntry(138, "Vehicle Maintenance"),
			new DomainEntry(89, "Vehicle Parts & Accessories"),
			new DomainEntry(473, "Vehicle Shopping"),
			new DomainEntry(803, "Vehicle Shows"),
		}),
		new DomainEntry(65, "Hobbies & Leisure", new DomainEntry[] { 
			new DomainEntry(189, "Clubs & Organizations"),
			new DomainEntry(1276, "Contests, Awards & Prizes"),
			new DomainEntry(284, "Crafts"),
			new DomainEntry(688, "Outdoors"),
			new DomainEntry(786, "Paintball"),
			new DomainEntry(787, "Radio Control & Modeling"),
			new DomainEntry(999, "Recreational Aviation"),
			new DomainEntry(977, "Special Occasions"),
			new DomainEntry(1002, "Water Activities"),
		}),
		new DomainEntry(66, "Pets & Animals", new DomainEntry[] { 
			new DomainEntry(882, "Animal Products & Services"),
			new DomainEntry(563, "Pets"),
			new DomainEntry(119, "Wildlife"),
		}),
		new DomainEntry(67, "Travel", new DomainEntry[] { 
			new DomainEntry(203, "Air Travel"),
			new DomainEntry(708, "Bus & Rail"),
			new DomainEntry(205, "Car Rental & Taxi Services"),
			new DomainEntry(1339, "Carpooling & Vehicle Sharing"),
			new DomainEntry(206, "Cruises & Charters"),
			new DomainEntry(179, "Hotels & Accommodations"),
			new DomainEntry(1003, "Luggage & Travel Accessories"),
			new DomainEntry(1004, "Specialty Travel"),
			new DomainEntry(208, "Tourist Destinations"),
			new DomainEntry(1010, "Travel Agencies & Services"),
			new DomainEntry(1011, "Travel Guides & Travelogues"),
		}),
		new DomainEntry(71, "Food & Drink", new DomainEntry[] { 
			new DomainEntry(560, "Beverages"),
			new DomainEntry(122, "Cooking & Recipes"),
			new DomainEntry(1512, "Food"),
			new DomainEntry(1523, "Food & Grocery Delivery"),
			new DomainEntry(121, "Food & Grocery Retailers"),
			new DomainEntry(276, "Restaurants"),
		}),
		new DomainEntry(174, "Science", new DomainEntry[] { 
			new DomainEntry(435, "Astronomy"),
			new DomainEntry(440, "Biological Sciences"),
			new DomainEntry(505, "Chemistry"),
			new DomainEntry(1227, "Computer Science"),
			new DomainEntry(1168, "Earth Sciences"),
			new DomainEntry(442, "Ecology & Environment"),
			new DomainEntry(231, "Engineering & Technology"),
			new DomainEntry(436, "Mathematics"),
			new DomainEntry(444, "Physics"),
			new DomainEntry(445, "Scientific Equipment"),
			new DomainEntry(446, "Scientific Institutions"),
		}),
		new DomainEntry(299, "Online Communities", new DomainEntry[] { 
			new DomainEntry(504, "Blogging Resources & Services"),
			new DomainEntry(55, "Dating & Personals"),
			new DomainEntry(1482, "Feed Aggregation & Social Bookmarking"),
			new DomainEntry(321, "File Sharing & Hosting"),
			new DomainEntry(191, "Forum & Chat Providers"),
			new DomainEntry(43, "Online Goodies"),
			new DomainEntry(582, "Online Journals & Personal Sites"),
			new DomainEntry(275, "Photo & Video Sharing"),
			new DomainEntry(529, "Social Networks"),
			new DomainEntry(972, "Virtual Worlds"),
		}),
		new DomainEntry(533, "Reference", new DomainEntry[] { 
			new DomainEntry(527, "Directories & Listings"),
			new DomainEntry(980, "General Reference"),
			new DomainEntry(1084, "Geographic Reference"),
			new DomainEntry(474, "Humanities"),
			new DomainEntry(108, "Language Resources"),
			new DomainEntry(375, "Libraries & Museums"),
			new DomainEntry(1233, "Technical Reference"),
		}),
		new DomainEntry(958, "Jobs & Education", new DomainEntry[] { 
			new DomainEntry(74, "Education"),
			new DomainEntry(1471, "Internships"),
			new DomainEntry(60, "Jobs"),
		}),
		new DomainEntry(5000, "World Localities", new DomainEntry[] { 
			new DomainEntry(5001, "Africa"),
			new DomainEntry(5028, "Asia"),
			new DomainEntry(5104, "Europe"),
			new DomainEntry(5203, "Latin America"),
			new DomainEntry(5588, "Middle East"),
			new DomainEntry(5237, "North America"),
			new DomainEntry(5335, "Oceania"),
			new DomainEntry(5353, "Polar Regions"),
		})
	};
}
