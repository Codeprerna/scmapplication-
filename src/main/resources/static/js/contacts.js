// console.log("Contacts.js");
// const baseURL = "http://localhost:8081";
const baseURL = "http://scmapplication.ap-south-1.elasticbeanstalk.com";
const viewContactModal = document.getElementById('view_contact_modal');
// options with default values
const options = {
    placement: 'bottom-right',
    backdrop: 'dynamic',
    backdropClasses:
        'bg-gray-900/50 dark:bg-gray-900/80 fixed inset-0 z-40',
    closable: true,
    onHide: () => {
        console.log('modal is hidden');
    },
    onShow: () => {
        console.log('modal is shown');
    },
    onToggle: () => {
        console.log('modal has been toggled');
    },
};

// instance options object
const instanceOptions = {
  id: 'view_contact_modal',
  override: true
};

const contactModal=new Modal(viewContactModal, options, instanceOptions);

function openContactModal(){
    contactModal.show();
}

function closeContactModal(){
    contactModal.hide();
}

async function loadContactData(id){
    //function call to load data

    console.log(id);    
    try {
        const data = await(await fetch(`${baseURL}/api/contacts/${id}`)).json();
        console.log(data);
       document.querySelector('#contact_name').innerHTML=data.name;
       document.querySelector("#contact_email").innerHTML= data.email;
       document.querySelector("#contact_image").src=data.picture;
       document.querySelector("#contact_address").innerHTML=data.address;
       document.querySelector("#contact_phone").innerHTML= data.phoneNumber;
       if(data.description){
        document.querySelector("#contact_about").innerHTML= data.description;
       }else{
        document.querySelector("#contact_about").innerHTML="No description available";
       }
       const contactFavourite = document.querySelector("#contact_favourite");
       if(data.favourite){
        contactFavourite.innerHTML=
        "<i class='fa-solid fa-heart' style='color: #ee3a91;'></i> "
       }else{
        contactFavourite.innerHTML= "Not Favourite Contact";
       }

      if(data.websiteLink){
        document.querySelector("#contact_website").href= data.websiteLink;
        document.querySelector("#contact_website").innerHTML= data.websiteLink;
      }else{
        document.querySelector("#contact_website").innerHTML= "No website link available";
      }
       if(data.linkedInLink){
        document.querySelector("#contact_linkedIn").href= data.linkedInLink;
       document.querySelector("#contact_linkedIn").innerHTML = data.linkedInLink;
       }else{
        document.querySelector("#contact_linkedIn").innerHTML = "No linkedin link available";
       }
       openContactModal();
    } catch (error) {
        console.log("Error:", error);
    }
   
    
}

// delete contact
async function deleteContact(id) {
    Swal.fire({
        title: "Do you want to delete the contact?",
        icon:"warning",
        showCancelButton: true,
        confirmButtonText: "Delete",
        confirmButtonColor: "#7066e0",
        cancelButtonColor:"#6e7881",
      }).then((result) => {

        /* Read more about isConfirmed, isDenied below */
        if (result.isConfirmed) {
         const url= `${baseURL}/user/contacts/delete/` + id;
         window.location.replace(url);

        }
      });
}

// showConfirmButton: true,  // Explicitly enable button
// confirmButtonColor: "#3085d6",  // Force a visible color
// confirmButtonText: "OK"