﻿//------------------------------------------------------------------------------
// <auto-generated>
//     This code was generated from a template.
//
//     Manual changes to this file may cause unexpected behavior in your application.
//     Manual changes to this file will be overwritten if the code is regenerated.
// </auto-generated>
//------------------------------------------------------------------------------

namespace DressExchangeServer
{
    using System;
    using System.Data.Entity;
    using System.Data.Entity.Infrastructure;
    
    public partial class Dress_DB : DbContext
    {
        public Dress_DB()
            : base("name=Dress_DB")
        {
        }
    
        protected override void OnModelCreating(DbModelBuilder modelBuilder)
        {
            throw new UnintentionalCodeFirstException();
        }
    
        public virtual DbSet<dress> dresses { get; set; }
        public virtual DbSet<dress_photo> dress_photo { get; set; }
        public virtual DbSet<like> likes { get; set; }
        public virtual DbSet<suburb> suburbs { get; set; }
        public virtual DbSet<user> users { get; set; }
    }
}
