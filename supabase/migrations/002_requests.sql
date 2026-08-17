create table if not exists public.requests (
    id uuid primary key default gen_random_uuid(),
    owner_id uuid not null references auth.users(id) on delete cascade,
    need text not null,
    offer text not null,
    description text,
    created_at timestamptz not null default now(),
    updated_at timestamptz not null default now()
);

alter table public.requests enable row level security;

revoke all on table public.requests from anon;
grant select, insert, update, delete on table public.requests to authenticated;

drop policy if exists "Authenticated users can read requests" on public.requests;
create policy "Authenticated users can read requests"
on public.requests for select to authenticated
using (true);

drop policy if exists "Users can insert their own requests" on public.requests;
create policy "Users can insert their own requests"
on public.requests for insert to authenticated
with check ((select auth.uid()) = owner_id);

drop policy if exists "Users can update their own requests" on public.requests;
create policy "Users can update their own requests"
on public.requests for update to authenticated
using ((select auth.uid()) = owner_id)
with check ((select auth.uid()) = owner_id);

drop policy if exists "Users can delete their own requests" on public.requests;
create policy "Users can delete their own requests"
on public.requests for delete to authenticated
using ((select auth.uid()) = owner_id);

create or replace function public.set_requests_updated_at()
returns trigger
language plpgsql
set search_path = ''
as $$
begin
    new.updated_at = now();
    return new;
end;
$$;

drop trigger if exists set_requests_updated_at on public.requests;
create trigger set_requests_updated_at
before update on public.requests
for each row execute function public.set_requests_updated_at();
