import { Icon } from "@/components/icons/Icons";
import {
	IconCodeAsterisk,
	IconHome2,
	IconCalculator,
	IconBuildingWarehouse,
	IconUsersGroup,
	IconTruckReturn,
	IconTruckDelivery,
	IconPresentation,
	IconUserEdit,
	IconReport
} from "@tabler/icons-react";

export interface MenuItem {
	icon: Icon;
	label: string;
	to: string;
	subMenu?: Omit<MenuItem, "icon" | "subMenu">[];
}

const defaultIcon = IconCodeAsterisk;

const menuItems: MenuItem[] = [
	{
		icon: IconHome2,
		label: "Dashboard",
		to: "/dashboard",
		subMenu: [
			{ label: "Overview", to: "/dashboard/overview" },
			{ label: "Sales Summary", to: "/dashboard/sales-summary" },
			{ label: "Activity Logs", to: "/dashboard/activity-logs" },
			{ label: "Performance Metrics", to: "/dashboard/performance-metrics" }
		]
	},
	{
		icon: IconCalculator,
		label: "Accounting System",
		to: "/accounting",
		subMenu: [
			{ label: "Accounts Payable", to: "/accounting/accounts-payable" },
			{ label: "Accounts Receivable", to: "/accounting/accounts-receivable" },
			{ label: "General Ledger", to: "/accounting/general-ledger" },
			{ label: "Budgeting", to: "/accounting/budgeting" },
			{ label: "Expense Management", to: "/accounting/expense-management" },
			{ label: "Financial Reports", to: "/accounting/financial-reports" }
		]
	},
	{
		icon: IconBuildingWarehouse,
		label: "Inventory Management",
		to: "/inventory",
		subMenu: [
			{ label: "Product Catalog", to: "/inventory/product-catalog" },
			{ label: "Stock Levels", to: "/inventory/stock-levels" },
			{ label: "Purchase Orders", to: "/inventory/purchase-orders" },
			{ label: "Warehouse Management", to: "/inventory/warehouse" },
			{ label: "Inventory Adjustment", to: "/inventory/adjustment" }
		]
	},
	{
		icon: IconUsersGroup,
		label: "Human Resources",
		to: "/hr",
		subMenu: [
			{ label: "Employee Directory", to: "/hr/employee-directory" },
			{ label: "Payroll Management", to: "/hr/payroll" },
			{ label: "Attendance & Leave", to: "/hr/attendance" },
			{ label: "Performance Reviews", to: "/hr/performance-reviews" },
			{ label: "Benefits & Compensation", to: "/hr/benefits" }
		]
	},
	{
		icon: IconTruckDelivery,
		label: "Sales",
		to: "/sales",
		subMenu: [
			{ label: "Leads Management", to: "/sales/leads" },
			{ label: "Opportunities", to: "/sales/opportunities" },
			{ label: "Sales Orders", to: "/sales/orders" },
			{ label: "Quotations", to: "/sales/quotations" },
			{ label: "Contracts", to: "/sales/contracts" }
		]
	},
	{
		icon: IconTruckReturn,
		label: "Purchasing",
		to: "/purchasing",
		subMenu: [
			{ label: "Supplier Management", to: "/purchasing/suppliers" },
			{ label: "Purchase Orders", to: "/purchasing/purchase-orders" },
			{ label: "Supplier Contracts", to: "/purchasing/contracts" },
			{ label: "Requisitions", to: "/purchasing/requisitions" }
		]
	},
	{
		icon: defaultIcon,
		label: "Customer Relationship Management",
		to: "/crm",
		subMenu: [
			{ label: "Customer Accounts", to: "/crm/accounts" },
			{ label: "Interaction History", to: "/crm/interactions" },
			{ label: "Support Tickets", to: "/crm/tickets" },
			{ label: "Loyalty Programs", to: "/crm/loyalty" }
		]
	},
	{
		icon: IconPresentation,
		label: "Project Management",
		to: "/projects",
		subMenu: [
			{ label: "Projects Overview", to: "/projects/overview" },
			{ label: "Task Management", to: "/projects/tasks" },
			{ label: "Resource Allocation", to: "/projects/resources" },
			{ label: "Project Budgets", to: "/projects/budgets" }
		]
	},
	{
		icon: IconUserEdit,
		label: "User Management",
		to: "/users",
		subMenu: [
			{ label: "User Roles & Permissions", to: "/users/roles" },
			{ label: "User Activity Tracking", to: "/users/activity" },
			{ label: "User Groups", to: "/users/groups" }
		]
	},
	{
		icon: IconReport,
		label: "Reports & Analytics",
		to: "/reports",
		subMenu: [
			{ label: "Custom Reports", to: "/reports/custom" },
			{ label: "KPI Dashboards", to: "/reports/kpis" },
			{ label: "Data Export", to: "/reports/export" }
		]
	}
];

export default menuItems;
