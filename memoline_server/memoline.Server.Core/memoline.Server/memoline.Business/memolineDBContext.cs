using memoline.Model;
using Microsoft.EntityFrameworkCore;
using Microsoft.Extensions.Configuration;
using System;
using System.Collections.Generic;
using System.Text;

namespace memoline.Business
{
    public class memolineDBContext:DbContext
    {
        string connectionString;

        public memolineDBContext(string connectionString)
        {
            this.connectionString = connectionString;
        }

        // public memolineDBContext()
        // {
        //     this.connectionString = "Host=37.148.211.192;Database=MemolineDB;Username=postgres;Password=1234";
        // }

        protected override void OnConfiguring(DbContextOptionsBuilder optionsBuilder)
        {
            // optionsBuilder.UseNpgsql("Host=localhost;Database=MemolineDB;Username=postgres;Password=1234");
            //optionsBuilder.UseNpgsql("Host=37.148.211.192;Database=MemolineTestDB;Username=postgres;Password=1234");
            optionsBuilder.UseNpgsql(this.connectionString);
            base.OnConfiguring(optionsBuilder);
        }

        protected override void OnModelCreating(ModelBuilder modelBuilder)
        {

            modelBuilder.Entity<ExceptionLog>()
                .Property(b => b.ExceptionLogID)
                .ValueGeneratedOnAdd();

            modelBuilder.Entity<Hashtag>()
                .Property(b => b.HashtagID)
                .ValueGeneratedOnAdd();

            modelBuilder.Entity<Note>()
                .Property(b => b.NoteID)
                .ValueGeneratedOnAdd();

            modelBuilder.Entity<User>()
                .Property(b => b.UserID)
                .ValueGeneratedOnAdd();

            modelBuilder.Entity<UserAccessKey>()
                .Property(b => b.UserID)
                .ValueGeneratedOnAdd();


            modelBuilder.Entity<NoteHashtag>().HasKey(sc => new { sc.NoteID, sc.HashtagID });
            //modelBuilder.Entity<Note>().HasMany(p => p.Hashtags);
            //modelBuilder.Entity<Hashtag>().HasMany(p => p.Notes);
            //modelBuilder.Entity<Conversation>().HasOne(p => p.GuestUser).WithOne().OnDelete(DeleteBehavior.Restrict);

            base.OnModelCreating(modelBuilder);
        }

        public DbSet<User> Users
        {
            get;
            set;
        }

        public DbSet<UserAccessKey> UserAccessKeys
        {
            get;
            set;
        }

        public DbSet<Note> Notes
        {
            get;
            set;
        }

        public DbSet<Hashtag> Hashtags
        {
            get;
            set;
        }

        public DbSet<NoteHashtag> NoteHashtags
        {
            get;
            set;
        }

        public DbSet<ExceptionLog> ExceptionLogs
        {
            get;
            set;
        }
    }
}
