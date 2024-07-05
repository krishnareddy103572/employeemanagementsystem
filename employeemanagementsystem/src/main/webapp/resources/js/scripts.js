document.addEventListener("DOMContentLoaded", function() {
	// Function to filter rows based on the search query
	window.searchTable = function() {
		var input, filter, table, tr, td, i, txtValue;
		input = document.getElementById("searchInput");
		filter = input.value.toUpperCase();
		table = document.getElementById("employeeTable");
		tr = table.getElementsByTagName("tr");

		// Loop through all table rows, and hide those who don't match the search query
		for (i = 1; i < tr.length; i++) {
			td = tr[i].getElementsByTagName("td");
			var showRow = false;

			for (var j = 0; j < td.length - 2; j++) {
				if (td[j]) {
					txtValue = td[j].textContent || td[j].innerText;
					if (txtValue.toUpperCase().indexOf(filter) > -1) {
						showRow = true;
						break;
					}
				}
			}

			if (showRow) {
				tr[i].style.display = "";
			} else {
				tr[i].style.display = "none";
			}
		}
	};

	var downloadSelector = document.getElementById("downloadSelector");
	downloadSelector.addEventListener('change', function() {
		if (this.value === 'excel') {
			downloadExcel();
		} else if (this.value === 'pdf') {
			downloadPDF();
		} else if (this.value === 'csv') {
			downloadCSV();
		}

		displayPage(currentPage);
		setupPagination();
	});

	// Define downloadExcel, downloadPDF and downloadCSV functions
	// ...
	function getTableData() {
		var table = document.querySelector('table');
		var data = [];
		var headers = [];
		table.querySelectorAll('th').forEach(function(header, index) {
			if (index < table.querySelectorAll('th').length - 2) { // Skip 'Edit' and 'Delete' columns
				headers.push(header.innerText);
			}
		});

		table
			.querySelectorAll('tr')
			.forEach(
				function(row, rowIndex) {
					if (rowIndex > 0) { // Skip header row
						var rowData = {};
						row
							.querySelectorAll('td')
							.forEach(
								function(cell, cellIndex) {
									if (cellIndex < headers.length) {
										rowData[headers[cellIndex]] = cell.innerText;
									}
								});
						data.push(rowData);
					}
				});
		console.log(data);
		return data;
	}

	function downloadExcel() {
		var employeeData = getTableData();
		var ws = XLSX.utils.json_to_sheet(employeeData);
		var wb = XLSX.utils.book_new();
		XLSX.utils.book_append_sheet(wb, ws, "Employees");
		XLSX.writeFile(wb, "EmployeeData.xlsx");
	}

	function downloadPDF() {
		var employeeData = getTableData();
		if (employeeData.length === 0) {
			console.error('No data available to download PDF');
			return;
		}
		console.log('Attempting to generate PDF');
		var doc = new jspdf.jsPDF({
			orientation: 'landscape'
		});

		doc.autoTable({
			head: [Object.keys(employeeData[0])],
			body: employeeData.map(Object.values),
			didDrawCell: function(data) {
				// Only change the font for header cells
				if (data.row.section === 'head') {
					doc.setFont('helvetica', 'bold');
				}
			},
			willDrawCell: function(data) {
				// Change the font back for body cells
				if (data.row.section === 'body') {
					doc.setFont('helvetica', 'normal');
				}
			},
		});

		// Debugging: Log the PDF blob
		console.log(doc.output('blob'));

		// Attempt to save PDF
		doc.save("EmployeeData.pdf");
	}
	function downloadCSV() {
		var employeeData = getTableData();
		var csvContent = "data:text/csv;charset=utf-8,";

		// Add header row
		csvContent += Object.keys(employeeData[0]).join(",") + "\r\n";

		// Add body rows
		employeeData.forEach(function(row) {
			csvContent += Object.values(row).join(",") + "\r\n";
		});

		// Create a link to download the CSV file
		var encodedUri = encodeURI(csvContent);
		var link = document.createElement("a");
		link.setAttribute("href", encodedUri);
		link.setAttribute("download", "EmployeeData.csv");
		document.body.appendChild(link); // Required for FF

		// Trigger CSV download
		link.click();
		document.body.removeChild(link); // Clean up after download
	}

	// Assuming you have a function to get all your table data
	var employeeData = getTableData();
	var currentPage = 1;
	var rowsPerPage = 10;

	function displayPage(page) {
		var table = document.getElementById("employeeTable").getElementsByTagName('tbody')[0];
		var start = (page - 1) * rowsPerPage;
		var end = start + rowsPerPage;
		var paginatedItems = employeeData.slice(start, end);

		// Clear out the table
		table.innerHTML = "";

		// Add the paginated rows to the table
		paginatedItems.forEach(function(row) {
			var tr = document.createElement('tr');
			Object.values(row).forEach(function(text) {
				var td = document.createElement('td');
				td.textContent = text;
				tr.appendChild(td);
			});
			table.appendChild(tr);
		});
	}

	function setupPagination() {
		var paginationElement = document.getElementById('pagination');
		var pageCount = Math.ceil(employeeData.length / rowsPerPage);

		for (var i = 1; i <= pageCount; i++) {
			var btn = paginationButton(i);
			paginationElement.appendChild(btn);
		}
	}

	function paginationButton(page) {
		var button = document.createElement('button');
		button.innerText = page;

		if (currentPage == page) button.classList.add('active');

		button.addEventListener('click', function() {
			currentPage = page;
			displayPage(currentPage);

			let currentBtn = document.querySelector('.pagenumbers button.active');
			currentBtn.classList.remove('active');

			button.classList.add('active');
		});

		return button;
	}

});

// Get the menu icon container element
const menuIconContainer = document.querySelector('.menu-icon-container');

// Add a click event listener to the menu icon container
menuIconContainer.addEventListener('click', () => {
  // Toggle the 'change' class on the menu icon container
  menuIconContainer.classList.toggle('change');
});

function toggleDropdown() {
  document.getElementById("myDropdown").classList.toggle("show");
}

// Close the dropdown menu if the user clicks outside of it
window.onclick = function(event) {
  if (!event.target.matches('.dropdown *')) { // * here includes the button itself and any children
    var dropdowns = document.getElementsByClassName("dropdown-content");
    var i;
    for (i = 0; i < dropdowns.length; i++) {
      var openDropdown = dropdowns[i];
      if (openDropdown.classList.contains('show')) {
        openDropdown.classList.remove('show');
      }
    }
  }
}